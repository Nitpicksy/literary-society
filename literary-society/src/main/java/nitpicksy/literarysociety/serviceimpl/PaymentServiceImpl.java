package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.ZuulClient;
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
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    private MerchantService merchantService;

    private TransactionService transactionService;

    private ZuulClient zuulClient;

    private MembershipService membershipService;

    private JWTTokenService jwtTokenService;

    @Override
    public String proceedToPayment(Set<Book> bookSet, User user) {
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
    public void handlePayment(LiterarySocietyOrderRequestDTO dto) {
        Transaction order = transactionService.findById(dto.getMerchantOrderId());

        if (dto.getStatus().equals("SUCCESS")) {
            order.setStatus(TransactionStatus.SUCCESS);
        } else if (dto.getStatus().equals("ERROR")) {
            order.setStatus(TransactionStatus.ERROR);
        } else {
            order.setStatus(TransactionStatus.FAILED);
        }

        transactionService.save(order);
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
    public PaymentServiceImpl(MerchantService merchantService, TransactionService transactionService, ZuulClient zuulClient,
                              MembershipService membershipService, JWTTokenService jwtTokenService) {
        this.merchantService = merchantService;
        this.transactionService = transactionService;
        this.zuulClient = zuulClient;
        this.membershipService = membershipService;
        this.jwtTokenService = jwtTokenService;
    }
}
