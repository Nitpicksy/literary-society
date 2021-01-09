package nitpicksy.paypalservice.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.client.ZuulClient;
import nitpicksy.paypalservice.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.paypalservice.dto.response.PaymentResponseDTO;
import nitpicksy.paypalservice.enumeration.TransactionStatus;
import nitpicksy.paypalservice.exceptionHandler.InvalidDataException;
import nitpicksy.paypalservice.model.Log;
import nitpicksy.paypalservice.model.PaymentRequest;
import nitpicksy.paypalservice.repository.PaymentRequestRepository;
import nitpicksy.paypalservice.service.LogService;
import nitpicksy.paypalservice.service.PaymentService;
import nitpicksy.paypalservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final String MODE = "sandbox";
    private static final String CONFIRMATION_URL = "https://localhost:8200/api/payments/confirm";

    //API Source: https://exchangerate.host/
    private static final String CURRENCY_CONVERSION_API = "https://api.exchangerate.host/convert";
    private static final Double BACKUP_CONVERSION_RATE = 0.01034;

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private PaymentRequestRepository paymentRequestRepository;

    private ZuulClient zuulClient;

    private LogService logService;

    private TransactionService transactionService;
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
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CRE",
                    String.format("PayPal payment for merchantOrderId=%s could not be created. PayPal error code: %s",
                            paymentRequest.getMerchantOrderId(), e.getDetails().getName())));
            throw new InvalidDataException("PayPal payment could not be initialized. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        String paymentId = approvedPayment.getId();
        String approvalUrl = getApprovalUrl(approvedPayment);

        paymentRequest.setPaymentId(paymentId);
        PaymentRequest savedPaymentRequest = paymentRequestRepository.save(paymentRequest);

        Instant today = (new Date()).toInstant();
        executeCancelledTransaction(savedPaymentRequest.getId(), today.plus(15, ChronoUnit.MINUTES));

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CRE",
                String.format("PayPal payment with paymentId=%s successfully created.", paymentId)));

        return new PaymentResponseDTO(savedPaymentRequest.getId(), approvalUrl);
    }

    @Override
    public String executePayment(String paymentId, String payerId) {
        PaymentRequest paymentRequest = paymentRequestRepository.findOneByPaymentId(paymentId);
        if (paymentRequest == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "EXE",
                    "Payment could not be continued due to invalid paymentId provided."));
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
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "EXE",
                    String.format("PayPal payment for merchantOrderId=%s could not be executed. PayPal error code: %s",
                            paymentRequest.getMerchantOrderId(), e.getDetails().getName())));
            ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentRequest.getId(), "ERROR");
            transactionService.create(paymentRequest, TransactionStatus.ERROR);
            sendResponseToPaymentGateway(paymentRequest.getMerchantOrderId(), confirmPaymentResponseDTO);
            return paymentRequest.getErrorURL();
        }

        if (!paymentResponse.getState().equals("approved")) {
            ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentRequest.getId(), "FAILED");
            transactionService.create(paymentRequest, TransactionStatus.FAILED);
            sendResponseToPaymentGateway(paymentRequest.getMerchantOrderId(), confirmPaymentResponseDTO);
            return paymentRequest.getFailedURL();
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "EXE",
                String.format("PayPal payment with paymentId=%s successfully executed.", paymentId)));

        ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentRequest.getId(), "SUCCESS");
        transactionService.create(paymentRequest, TransactionStatus.SUCCESS);
        sendResponseToPaymentGateway(paymentRequest.getMerchantOrderId(), confirmPaymentResponseDTO);

        return paymentRequest.getSuccessURL();
    }

    @Async
    public void sendResponseToPaymentGateway(String merchantOrderId, ConfirmPaymentResponseDTO confirmPaymentResponseDTO) {
        try{
            zuulClient.confirmPayment(merchantOrderId, confirmPaymentResponseDTO);
        }catch (RuntimeException e){
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", "Could not notify Payment Gateway"));
        }

    }

    @Async
    public void executeCancelledTransaction(Long paymentId, Instant executionMoment) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduler.schedule(createRunnable(paymentId), executionMoment);
    }


    private Runnable createRunnable(final Long paymentId) {
        return () -> {
            nitpicksy.paypalservice.model.Transaction transaction = transactionService.findByPaymentId(paymentId);

            PaymentRequest paymentRequest = paymentRequestRepository.findOneById(paymentId);
            if (paymentRequest != null && transaction == null) {
                transaction = transactionService.createErrorTransaction(paymentRequest);

                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SRQ",
                        String.format(
                                "Because 15min from creation have passed, transaction %s is automatically CANCELED",
                                transaction.getId())));
            }
        };
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
        BigDecimal bd1 = new BigDecimal(baseAmount * BACKUP_CONVERSION_RATE);
        String convertedAmount = bd1.setScale(2, RoundingMode.HALF_UP).toPlainString();

        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(CURRENCY_CONVERSION_API)
                .queryParam("from", "RSD")
                .queryParam("to", "USD")
                .queryParam("amount", baseAmount);

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
        } catch (RestClientException e) {
            response = null;
        }

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String result = root.path("result").asText();
                if (!result.isBlank()) {
                    BigDecimal bd2 = new BigDecimal(result);
                    convertedAmount = bd2.setScale(2, RoundingMode.HALF_UP).toPlainString();
                } else {
                    logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONV",
                            "Currency conversion API response changed. Backup conversion rate applied."));
                }
            } catch (JsonProcessingException e) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONV",
                        "Currency conversion API returned invalid JSON response. Backup conversion rate applied."));
            }
        } else {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONV",
                    "Currency conversion API response status is not 200 OK. Backup conversion rate applied."));
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
    public PaymentServiceImpl(PaymentRequestRepository paymentRequestRepository, ZuulClient zuulClient, LogService logService,
                              TransactionService transactionService) {
        this.paymentRequestRepository = paymentRequestRepository;
        this.zuulClient = zuulClient;
        this.logService = logService;
        this.transactionService = transactionService;
    }
}
