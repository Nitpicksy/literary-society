package nitpicksy.paypalservice.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;
import nitpicksy.paypalservice.exceptionHandler.InvalidDataException;
import nitpicksy.paypalservice.model.PaymentRequest;
import nitpicksy.paypalservice.repository.PaymentRequestRepository;
import nitpicksy.paypalservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String MODE = "sandbox";
    private static final String CONFIRMATION_URL = "http://localhost:8200/api/payments/confirm";

    //API Source: https://exchangerate.host/
    private static final String CURRENCY_CONVERSION_API = "https://api.exchangerate.host/convert";
    private static final Double BACKUP_CONVERSION_RATE = 0.01034;

    private PaymentRequestRepository paymentRequestRepository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequest paymentRequest) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(getHomepageURL(paymentRequest.getSuccessURL()));
        redirectUrls.setReturnUrl(CONFIRMATION_URL);

        Payment payment = new Payment();
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(getTransactionInformation(paymentRequest.getAmount()));
        payment.setIntent("sale");

        APIContext apiContext = new APIContext(
                paymentRequest.getMerchantClientId(),
                paymentRequest.getMerchantClientSecret(),
                MODE);

        Payment approvedPayment;
        try {
            approvedPayment = payment.create(apiContext);
        } catch (PayPalRESTException e) {
            throw new InvalidDataException("PayPal payment could not be initialized. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        String paymentId = approvedPayment.getId();
        String approvalUrl = getApprovalUrl(approvedPayment);

        paymentRequest.setPaymentId(paymentId);
        paymentRequestRepository.save(paymentRequest);

        return new PaymentResponseDTO(paymentId, approvalUrl);
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        PaymentRequest paymentRequest = paymentRequestRepository.findOneByPaymentId(paymentId);
        if (paymentRequest == null) {
            throw new InvalidDataException("Payment could not be continued due to invalid payment id provided.", HttpStatus.BAD_REQUEST);
        }

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment payment = new Payment().setId(paymentId);

        APIContext apiContext = new APIContext(
                paymentRequest.getMerchantClientId(),
                paymentRequest.getMerchantClientSecret(),
                MODE);

        Payment paymentResponse;
        try {
            paymentResponse = payment.execute(apiContext, paymentExecution);
        } catch (PayPalRESTException e) {
            return paymentRequest.getErrorURL();
        }

        if (!paymentResponse.getState().equals("approved")) {
            return paymentRequest.getFailedURL();
        }

        return paymentRequest.getSuccessURL();
    }

    private List<Transaction> getTransactionInformation(Double baseAmount) {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        String convertedAmount = convertCurrency(baseAmount);
        amount.setTotal(convertedAmount);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);

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
                .queryParam("from", "RSD")
                .queryParam("to", "USD")
                .queryParam("amount", baseAmount);

        ResponseEntity<String> response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);

        String convertedAmount = String.valueOf(baseAmount * BACKUP_CONVERSION_RATE);
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String result = root.path("result").asText();
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

    private String getHomepageURL(String fullURL) {
        String baseURL;
        try {
            URL url = URI.create(fullURL).toURL();
            baseURL = url.getProtocol() + "://" + url.getAuthority();
        } catch (MalformedURLException e) {
            baseURL = fullURL;
        }
        return baseURL;
    }

    @Autowired
    public PaymentServiceImpl(PaymentRequestRepository paymentRequestRepository) {
        this.paymentRequestRepository = paymentRequestRepository;
    }

}
