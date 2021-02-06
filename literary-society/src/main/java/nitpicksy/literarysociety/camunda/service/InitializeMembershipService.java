package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.enumeration.TransactionType;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PriceList;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.service.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class InitializeMembershipService implements JavaDelegate {

    private UserService userService;
    private PriceListService priceListService;
    private MerchantService merchantService;
    private TransactionService transactionService;
    private ZuulClient zuulClient;
    private JWTTokenService jwtTokenService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String username = (String) execution.getVariable("user");
        Writer writer = (Writer) userService.findByUsername(username);

        PriceList priceList = priceListService.findLatestPriceList();
        Merchant merchant = merchantService.findOurMerchant(); //lu merchant

        Double amount = priceList.getMembershipForWriter();

        Transaction transaction = transactionService.create(TransactionStatus.CREATED, TransactionType.MEMBERSHIP, writer, amount,
                null, merchant);

        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            // Send JWT token for authentication in Payment Gateway
            String url = zuulClient.pay("Bearer " + jwtTokenService.getToken(), new PaymentGatewayPayRequestDTO(transaction.getId(), merchant.getName(), amount, timestamp.toString()));

            execution.setVariable("url", url);
            execution.setVariable("successfulRequest", true);
        } catch (RuntimeException exception) {

            transaction.setStatus(TransactionStatus.ERROR);
            transactionService.save(transaction);
            execution.setVariable("successfulRequest", false);
            throw new InvalidUserDataException("Something went wrong. Please try again.", HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public InitializeMembershipService(UserService userService, PriceListService priceListService, MerchantService merchantService, TransactionService transactionService, ZuulClient zuulClient, JWTTokenService jwtTokenService) {
        this.userService = userService;
        this.priceListService = priceListService;
        this.merchantService = merchantService;
        this.transactionService = transactionService;
        this.zuulClient = zuulClient;
        this.jwtTokenService = jwtTokenService;
    }
}
