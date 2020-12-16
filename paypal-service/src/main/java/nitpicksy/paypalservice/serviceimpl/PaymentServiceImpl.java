package nitpicksy.paypalservice.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.dto.request.ConfirmPaymentDTO;
import nitpicksy.paypalservice.dto.request.PaymentRequestDTO;
import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;
import nitpicksy.paypalservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String MODE = "sandbox";

    //API Source: https://exchangerate.host/
    private static final String CURRENCY_CONVERSION_API = "https://api.exchangerate.host/convert";
    private static final String FROM_CURRENCY = "RSD";
    private static final String TO_CURRENCY = "USD";
    private static final String RESULT_NODE = "result";
    private static final Double BACKUP_CONVERSION_RATE = 0.01034;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) throws PayPalRESTException {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment paymentRequest = new Payment();
        paymentRequest.setPayer(payer);
        paymentRequest.setRedirectUrls(getRedirectURLs());
        paymentRequest.setTransactions(getTransactionInformation(paymentRequestDTO.getAmount()));
        paymentRequest.setIntent("authorize");

        APIContext apiContext = new APIContext(
                paymentRequestDTO.getPaymentDetails().getMerchantClientId(),
                paymentRequestDTO.getPaymentDetails().getMerchantClientSecret(),
                MODE);

        Payment approvedPayment = paymentRequest.create(apiContext);

        System.out.println("=== CREATED PAYMENT: ====");
        System.out.println(approvedPayment);

        String paymentId = approvedPayment.getId();
        String approvalUrl = getApprovalUrl(approvedPayment);

        return new PaymentResponseDTO(paymentId, approvalUrl);
    }

    public String completePayment(String paymentId, ConfirmPaymentDTO confirmPaymentDTO) throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(confirmPaymentDTO.getPayerId());

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(
                confirmPaymentDTO.getPaymentDetails().getMerchantClientId(),
                confirmPaymentDTO.getPaymentDetails().getMerchantClientSecret(),
                MODE);

        payment.execute(apiContext, paymentExecution);

        System.out.println("=== EXECUTED PAYMENT: ====");
        System.out.println(payment);

        return "SuccessURL";
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:3000/cancel-payment");
        redirectUrls.setReturnUrl("http://localhost:3000/review-payment");

        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(Double baseAmount) {
        Amount amount = new Amount();
        amount.setCurrency(TO_CURRENCY);
        String convertedAmount = convertCurrency(baseAmount);
        amount.setTotal(convertedAmount);
//        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
//        transaction.setDescription(orderDetail.getProductName());

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);

        return listTransaction;
    }

    private String getApprovalUrl(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;

        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }

        return approvalLink;
    }

    private String convertCurrency(Double baseAmount) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(CURRENCY_CONVERSION_API)
                .queryParam("from", FROM_CURRENCY)
                .queryParam("to", TO_CURRENCY)
                .queryParam("amount", baseAmount);

        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);

        String convertedAmount = String.valueOf(baseAmount * BACKUP_CONVERSION_RATE);
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String result = root.path(RESULT_NODE).asText();
                if (result != null) {
                    BigDecimal bd = new BigDecimal(result);
                    convertedAmount = bd.setScale(2, RoundingMode.HALF_UP).toPlainString();
                }
            } catch (JsonProcessingException e) {
                convertedAmount = String.valueOf(baseAmount * BACKUP_CONVERSION_RATE);
            }
        }

        return convertedAmount;
    }
}
