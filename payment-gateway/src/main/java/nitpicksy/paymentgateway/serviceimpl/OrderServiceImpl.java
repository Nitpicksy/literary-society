package nitpicksy.paymentgateway.serviceimpl;

import feign.FeignException;
import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.request.PaymentRequestDTO;
import nitpicksy.paymentgateway.dto.response.LiterarySocietyOrderResponseDTO;
import nitpicksy.paymentgateway.dto.response.PaymentResponseDTO;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.mapper.ForwardRequestMapper;
import nitpicksy.paymentgateway.model.*;
import nitpicksy.paymentgateway.repository.DataForPaymentRepository;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.repository.TransactionRepository;
import nitpicksy.paymentgateway.service.CompanyService;
import nitpicksy.paymentgateway.service.LogService;
import nitpicksy.paymentgateway.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    @Value("${GATEWAY_PAYMENT_REDIRECT_URL}")
    private String gatewayRedirectUrl;

    private TransactionRepository transactionRepository;
    private CompanyService companyService;
    private PaymentMethodRepository paymentMethodRepository;
    private ForwardRequestMapper forwardRequestMapper;
    private DataForPaymentRepository dataForPaymentRepository;
    private ZuulClient zuulClient;
    private LogService logService;

    @Override
    public String createOrder(OrderRequestDTO orderDTO) {

        //TODO: based on certificate? for now we only have 1 literary society
        Company company = companyService.findCompanyByCommonName("literary-society");

        if (company == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", "Company not found"));
            throw new InvalidDataException("Company not found.", HttpStatus.BAD_REQUEST);
        }

        Merchant orderMerchant = company.getMerchant().stream()
                .filter(merchant -> merchant.getName().equals(orderDTO.getMerchantName())).findAny().orElse(null);

        if (orderMerchant == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", "Merchant not found"));
            throw new InvalidDataException("Merchant not found.", HttpStatus.BAD_REQUEST);
        }

        Transaction order = createTransaction(orderDTO, orderMerchant, company);


        String url = gatewayRedirectUrl + "/" + order.getId();
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Order %s is successfully created and redirect url is %s", order.getId(), url)));
        return url;
    }

    @Override
    public String forwardPaymentRequest(PaymentRequestDTO paymentRequestDTO) {

        Transaction transaction = findOrder(paymentRequestDTO.getOrderId());
        if (transaction == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", "Invalid transaction"));
            throw new InvalidDataException("Invalid transaction.", HttpStatus.BAD_REQUEST);
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findByCommonName(paymentRequestDTO.getPaymentCommonName());
        if (paymentMethod == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", "Invalid Payment Method ID"));
            throw new InvalidDataException("Invalid Payment Method ID.", HttpStatus.BAD_REQUEST);
        }

        //set payment method to transaction
        transaction.setPaymentMethod(paymentMethod);

        //dataForPayment for transaction merchant and his payment method
        List<DataForPayment> dataForPayments = dataForPaymentRepository.findDataForPaymentByMerchantAndPaymentMethod(transaction.getMerchant().getId(), paymentMethod.getId());

        //start mapping values to the requestDTO
        DynamicPaymentDetailsDTO forwardDTO = forwardRequestMapper.toDto(transaction);

        //dynamically setting paymentDetails.
        for (DataForPayment dataForPayment : dataForPayments) {
            forwardDTO.setDetails(dataForPayment.getAttributeName(), dataForPayment.getAttributeValue());
        }

        String responseURL;
        try {
            PaymentResponseDTO dto = zuulClient.forwardPaymentRequest(URI.create(apiGatewayURL + '/' + paymentRequestDTO.getPaymentCommonName()), forwardDTO);
            setPayment(paymentRequestDTO.getOrderId(), dto.getPaymentId());
            responseURL = dto.getPaymentURL();
        } catch (FeignException.FeignClientException e) {
            e.printStackTrace();
            //if bank request fails, redirect user to the company failedURL;
            cancelOrder(paymentRequestDTO.getOrderId());
            responseURL = forwardDTO.getFailedURL();
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Order forward has failed and response URL is %s", responseURL)));
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Order is successfully forwarded to payment service and redirect url is %s", responseURL)));
        return responseURL;
    }

    @Override
    public Transaction findOrder(Long orderId) {
        return transactionRepository.findById(orderId).orElse(null);
    }


    @Override
    public void cancelOrder(Long orderId) {
        transactionRepository.findById(orderId).ifPresent(transaction -> transaction.setStatus(TransactionStatus.CANCELED));
    }


    @Override
    public void setPayment(Long orderId, Long paymentId) {
        Transaction transaction = transactionRepository.findById(orderId).orElse(null);
        if (transaction == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Transaction null when receiving invoice from payment service.")));
            throw new InvalidDataException("Transaction null when receiving invoice from payment service.", HttpStatus.BAD_REQUEST);
        }
        transaction.setPaymentId(paymentId);
        transaction.setStatus(TransactionStatus.APPROVED);
        transactionRepository.save(transaction);
    }

    @Override
    public void handleConfirmPayment(Long merchantOrderId, ConfirmPaymentRequestDTO dto) {
        Transaction order = transactionRepository.findTransactionByMerchantOrderIdAndPaymentId(merchantOrderId, dto.getPaymentId());

        handleOrder(merchantOrderId, dto.getPaymentId(), dto.getStatus());

        LiterarySocietyOrderResponseDTO responseDTO = new LiterarySocietyOrderResponseDTO(order.getMerchantOrderId(), dto.getStatus());

        String companyCommonName = order.getCompany().getCommonName();

        try {

            zuulClient.confirmPaymentToLiterarySociety(URI.create(apiGatewayURL + '/' + companyCommonName), responseDTO);

        } catch (FeignException.FeignClientException e) {
            e.printStackTrace();
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Went wrong when contacting the Literary Society.")));
            System.out.println("Went wrong when contacting the Literary Society.");
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Order with id %s is done and payment is successfully forwarded to Literary Society", order.getId())));

    }

    private void handleOrder(Long merchantOrderId, Long paymentId, String status) {
        Transaction transaction = transactionRepository.findTransactionByMerchantOrderIdAndPaymentId(merchantOrderId, paymentId);

        if (transaction == null) {
            return;
        }

        if (status.equals("SUCCESS")) {
            transaction.setStatus(TransactionStatus.COMPLETED);
        } else {
            transaction.setStatus(TransactionStatus.REJECTED);
        }

        transactionRepository.save(transaction);
    }


    private Transaction createTransaction(OrderRequestDTO orderRequestDTO, Merchant merchant, Company company) {
        Transaction transaction = new Transaction();
        transaction.setCompany(company);
        transaction.setAmount(orderRequestDTO.getAmount());
        transaction.setMerchantTimestamp(Timestamp.valueOf(orderRequestDTO.getTimestamp()));
        transaction.setMerchantOrderId(orderRequestDTO.getOrderId());
        transaction.setStatus(TransactionStatus.CREATED);
        transaction.setMerchant(merchant);
        transaction.setPaymentMethod(null);
        transaction.setPaymentId(null);

        System.out.println("Transaction - " + transaction);
        return transactionRepository.save(transaction);
    }

    @Autowired
    public OrderServiceImpl(LogService logService, TransactionRepository transactionRepository, CompanyService companyService, PaymentMethodRepository paymentMethodRepository, ForwardRequestMapper forwardRequestMapper, DataForPaymentRepository dataForPaymentRepository, ZuulClient zuulClient) {
        this.logService = logService;
        this.transactionRepository = transactionRepository;
        this.companyService = companyService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.forwardRequestMapper = forwardRequestMapper;
        this.dataForPaymentRepository = dataForPaymentRepository;
        this.zuulClient = zuulClient;
    }
}
