package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.PaymentGatewayClient;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.enumeration.TransactionType;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.MembershipService;
import nitpicksy.literarysociety.service.MerchantService;
import nitpicksy.literarysociety.service.PaymentService;
import nitpicksy.literarysociety.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PaymentServiceImpl implements PaymentService {

    private MerchantService merchantService;

    private TransactionService transactionService;

    private PaymentGatewayClient paymentGatewayClient;

    private MembershipService membershipService;

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
            return paymentGatewayClient.pay(new PaymentGatewayPayRequestDTO(transaction.getId(), merchant.getName(), amount));
        } catch (RuntimeException exception) {
            transaction.setStatus(TransactionStatus.ERROR);
            transactionService.save(transaction);
            throw new InvalidUserDataException("Something went wrong. Please try again.", HttpStatus.BAD_REQUEST);
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
    public PaymentServiceImpl(MerchantService merchantService, TransactionService transactionService, PaymentGatewayClient paymentGatewayClient,
                              MembershipService membershipService) {
        this.merchantService = merchantService;
        this.transactionService = transactionService;
        this.paymentGatewayClient = paymentGatewayClient;
        this.membershipService = membershipService;
    }
}
