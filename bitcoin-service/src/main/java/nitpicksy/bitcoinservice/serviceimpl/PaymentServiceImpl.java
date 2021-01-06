package nitpicksy.bitcoinservice.serviceimpl;

import nitpicksy.bitcoinservice.client.ZuulClient;
import nitpicksy.bitcoinservice.dto.request.CreateOrderBTCDTO;
import nitpicksy.bitcoinservice.dto.request.PaymentCallbackDTO;
import nitpicksy.bitcoinservice.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.bitcoinservice.dto.response.CreateOrderResponseBTCDTO;
import nitpicksy.bitcoinservice.dto.response.PaymentResponseDTO;
import nitpicksy.bitcoinservice.enumeration.PaymentStatus;
import nitpicksy.bitcoinservice.exceptionHandler.InvalidDataException;
import nitpicksy.bitcoinservice.model.Log;
import nitpicksy.bitcoinservice.model.Payment;
import nitpicksy.bitcoinservice.repository.PaymentRepository;
import nitpicksy.bitcoinservice.service.CurrencyService;
import nitpicksy.bitcoinservice.service.LogService;
import nitpicksy.bitcoinservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${SANDBOX_ENVIRONMENT}")
    private String sandbox;

    @Value("${CALLBACK_URL}")
    private String callback;

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private final String AUTHORIZATION_HEADER = "Authorization";

    private LogService logService;
    private RestTemplate restTemplate;
    private CurrencyService currencyService;
    private PaymentRepository paymentRepository;
    private ZuulClient zuulClient;


    @Override
    public PaymentResponseDTO createPayment(Payment paymentRequest) {

        if (paymentRequest == null) {
            throw new InvalidDataException("Something went wrong with creating a payment request", HttpStatus.BAD_REQUEST);
        }

        Double amount = Double.valueOf(currencyService.convertCurrency(paymentRequest.getAmount()));

        CreateOrderBTCDTO orderBTCDTO = new CreateOrderBTCDTO(paymentRequest.getId().toString(),
                amount, "USD", "BTC", callback,
                paymentRequest.getErrorURL(), paymentRequest.getSuccessURL(),
                paymentRequest.getMerchantToken());

        HttpHeaders headers = new HttpHeaders();
        headers.add(this.AUTHORIZATION_HEADER, "Bearer " + paymentRequest.getMerchantToken());
        HttpEntity<CreateOrderBTCDTO> request = new HttpEntity<>(orderBTCDTO, headers);

        ResponseEntity<CreateOrderResponseBTCDTO> response = null;

        try {
            response = restTemplate.exchange(sandbox + "/orders", HttpMethod.POST, request, CreateOrderResponseBTCDTO.class);
        } catch (Exception e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", "Create order CoinGate request failed"));
            throw new InvalidDataException("Create order CoinGate failed", HttpStatus.BAD_REQUEST);
        }

        CreateOrderResponseBTCDTO responseObject = response.getBody();
        if (responseObject == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", "Create order CoinGate response failed"));
            throw new InvalidDataException("Error when fetching CoinGate response", HttpStatus.BAD_REQUEST);

        }

        Payment updatedPayment = setPayment(paymentRequest, responseObject);
        if (updatedPayment == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", "Transaction save failed"));
            throw new InvalidDataException("Went wrong when saving the payment from CoinGate", HttpStatus.BAD_REQUEST);
        }

        return new PaymentResponseDTO(paymentRequest.getId(), responseObject.getPayment_url());
    }


    private Payment setPayment(Payment payment, CreateOrderResponseBTCDTO dto) {
        payment.setCreationDate(dto.getCreated_at());
        payment.setPaymentId(dto.getId());
        payment.setStatus(PaymentStatus.valueOf(dto.getStatus().toUpperCase()));
        if (!dto.getReceive_amount().equals("")) {
            try {
                payment.setReceivedAmount(Double.parseDouble(dto.getReceive_amount()));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        payment.setReceiveCurrency(dto.getReceive_currency());
        return savePayment(payment);
    }

    public Payment savePayment(Payment payment) {
        Payment attempt;
        try {
            attempt = paymentRepository.save(payment);
        } catch (Exception e) {
            return null;
        }
        return attempt;
    }

    @Override
    public void executePayment(PaymentCallbackDTO callbackDTO) {
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", "Executing CoinGate callback with order id" + callbackDTO.getOrder_id()));

        Payment payment = paymentRepository.findById(Long.valueOf(callbackDTO.getOrder_id())).orElse(null);
        if (payment == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", "Could not find payment for the given id"));
            throw new InvalidDataException("Could not find payment for the given id", HttpStatus.BAD_REQUEST);
        }

        payment.setStatus(PaymentStatus.valueOf(callbackDTO.getStatus().toUpperCase()));
        payment.setReceivedAmount(Double.valueOf(callbackDTO.getReceive_amount()));
        paymentRepository.save(payment);

        try {
            if (callbackDTO.getStatus().toUpperCase().equals(PaymentStatus.PAID.toString())) {
                notifyPaymentGateway(payment.getMerchantOrderId(), new ConfirmPaymentResponseDTO(payment.getId(), "SUCCESS"));
            } else if (callbackDTO.getStatus().toUpperCase().equals(PaymentStatus.CANCELED.toString()) ||
                    callbackDTO.getStatus().toUpperCase().equals(PaymentStatus.EXPIRED.toString()) ||
                    callbackDTO.getStatus().toUpperCase().equals(PaymentStatus.INVALID.toString())
            ) {
                notifyPaymentGateway(payment.getMerchantOrderId(), new ConfirmPaymentResponseDTO(payment.getId(), "FAILED"));
            }

        } catch (Exception e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", "Could not notify Payment Gateway"));
            throw new InvalidDataException("Could not notify Payment Gateway", HttpStatus.BAD_REQUEST);
        }
    }

    private void notifyPaymentGateway(Long merchantOrderId, ConfirmPaymentResponseDTO confirmPaymentResponseDTO) {
        zuulClient.confirmPayment(merchantOrderId, confirmPaymentResponseDTO);
    }


    @Autowired
    public PaymentServiceImpl(LogService logService, RestTemplate restTemplate, CurrencyService currencyService, PaymentRepository paymentRepository, ZuulClient zuulClient) {
        this.logService = logService;
        this.restTemplate = restTemplate;
        this.currencyService = currencyService;
        this.paymentRepository = paymentRepository;
        this.zuulClient = zuulClient;
    }
}
