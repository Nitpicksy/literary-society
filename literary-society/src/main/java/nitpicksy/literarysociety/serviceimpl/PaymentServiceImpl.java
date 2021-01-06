package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.camunda.service.CamundaService;
import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.constants.CamundaConstants;
import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.LiterarySocietyOrderRequestDTO;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.enumeration.TransactionType;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.*;
import nitpicksy.literarysociety.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {

    private MerchantService merchantService;

    private TransactionService transactionService;

    private ZuulClient zuulClient;

    private MembershipService membershipService;

    private JWTTokenService jwtTokenService;

    private PriceListService priceListService;

    private CamundaService camundaService;

    @Override
    public String proceedToBookPayment(Set<Book> bookSet, User user) {
        List<Book> bookList = new ArrayList<>(bookSet);
        Merchant merchant = merchantService.findByName(bookList.get(0).getPublishingInfo().getMerchant().getName());
        if (merchant == null) {
            throw new InvalidUserDataException("Merchant doesn't exist.", HttpStatus.BAD_REQUEST);
        }
        Double amount = calculatePrice(bookList, user);
        Transaction transaction = transactionService.create(TransactionStatus.CREATED, TransactionType.ORDER, user, amount,
                new HashSet<>(bookList), merchant);
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            // Send JWT token for authentication in Payment Gateway
            return zuulClient.pay("Bearer " + jwtTokenService.getToken(), new PaymentGatewayPayRequestDTO(transaction.getId(), merchant.getName(), amount, timestamp.toString()));
        } catch (RuntimeException exception) {
            transaction.setStatus(TransactionStatus.ERROR);
            transactionService.save(transaction);
            throw new InvalidUserDataException("Something went wrong. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String proceedToMembershipPayment(User user) {

        if (user == null) {
            throw new InvalidUserDataException("Session expired, please log in.", HttpStatus.BAD_REQUEST);
        }

        PriceList priceList = priceListService.findLatestPriceList();

        Merchant merchant = merchantService.findOurMerchant(); //lu merchant

        Double amount;
        if (user.getRole().getName().equals(RoleConstants.ROLE_READER)) {
            amount = priceList.getMembershipForReader();
        } else {
            amount = priceList.getMembershipForWriter();
        }

        Transaction transaction = transactionService.create(TransactionStatus.CREATED, TransactionType.MEMBERSHIP, user, amount,
                null, merchant);
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            // Send JWT token for authentication in Payment Gateway
            return zuulClient.pay("Bearer " + jwtTokenService.getToken(), new PaymentGatewayPayRequestDTO(transaction.getId(), merchant.getName(), amount, timestamp.toString()));
        } catch (RuntimeException exception) {
            transaction.setStatus(TransactionStatus.ERROR);
            transactionService.save(transaction);
            throw new InvalidUserDataException("Something went wrong. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void handlePayment(LiterarySocietyOrderRequestDTO dto) {
        Transaction order = transactionService.findById(dto.getMerchantOrderId());
        User user = order.getBuyer();

        if (dto.getStatus().equals("SUCCESS")) {
            order.setStatus(TransactionStatus.SUCCESS);

            if (order.getType().equals(TransactionType.ORDER)) {
                if (user.getRole().getName().equals(RoleConstants.ROLE_READER)) {
                    Reader reader = (Reader) user;
                    reader.setPurchasedBooks(order.getOrderedBooks());
                }
            } else {
                if (user.getRole().getName().equals(RoleConstants.ROLE_WRITER)) {
                    notifyCamundaMessageEvent(CamundaConstants.MESSAGE_PAYMENT_SUCCESS, user, order);
                } else {
                    Merchant merchant = merchantService.findOurMerchant();
                    Membership membership = membershipService.createMembership(user, merchant);
                    order.setMembership(membership);
                }

            }
        } else if (dto.getStatus().equals("ERROR")) {
            order.setStatus(TransactionStatus.ERROR);
            if (user.getRole().getName().equals(RoleConstants.ROLE_WRITER)) {
                notifyCamundaMessageEvent(CamundaConstants.MESSAGE_PAYMENT_ERROR, user, order);
            }
        } else {
            order.setStatus(TransactionStatus.FAILED);
            if (user.getRole().getName().equals(RoleConstants.ROLE_WRITER)) {
                notifyCamundaMessageEvent(CamundaConstants.MESSAGE_PAYMENT_ERROR, user, order);
            }
        }
        transactionService.save(order);
    }

    private void notifyCamundaMessageEvent(String message, User user, Transaction order) {
        try {
            camundaService.messageEventReceived(CamundaConstants.MESSAGE_PAYMENT_SUCCESS, user.getUsername());
        } catch (Exception e) {
            //not a camunda process, carry on as usual
            Merchant merchant = merchantService.findOurMerchant();
            Membership membership = membershipService.createMembership(user, merchant);
            order.setMembership(membership);
        }
    }

    private double calculatePrice(List<Book> bookList, User user) {
        boolean includeDiscount = membershipService.checkIfUserMembershipIsValid(user.getUserId());
        Double amount = 0.0;
        for (Book book : bookList) {
            if (includeDiscount) {
                amount += (book.getPublishingInfo().getPrice() * (100.0 - book.getPublishingInfo().getDiscount()) / 100);
            } else {
                amount += book.getPublishingInfo().getPrice();
            }
        }
        return amount;
    }

    @Autowired
    public PaymentServiceImpl(MerchantService merchantService, TransactionService transactionService, ZuulClient zuulClient, MembershipService membershipService, JWTTokenService jwtTokenService, PriceListService priceListService, CamundaService camundaService) {
        this.merchantService = merchantService;
        this.transactionService = transactionService;
        this.zuulClient = zuulClient;
        this.membershipService = membershipService;
        this.jwtTokenService = jwtTokenService;
        this.priceListService = priceListService;
        this.camundaService = camundaService;
    }
}
