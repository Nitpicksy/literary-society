package nitpicksy.paymentgateway.serviceimpl;

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
import nitpicksy.paymentgateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    private UserService userService;

    @Override
    public String createOrder(OrderRequestDTO orderDTO) {

        Company company = userService.getAuthenticatedCompany();

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

        Instant today = (new Date()).toInstant();
        executeCancelledTransaction(order.getId(), today.plus(15, ChronoUnit.MINUTES));

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
        } catch (RuntimeException e) {
            //if bank request fails, redirect user to the company failedURL;
            setTransactionStatus(transaction,TransactionStatus.CANCELED);
            notifyCompany(transaction,"ERROR" );

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
    public void handleConfirmPayment(String merchantOrderId, ConfirmPaymentRequestDTO dto) {
        Transaction order = transactionRepository.findTransactionByMerchantOrderIdAndPaymentId(merchantOrderId, dto.getPaymentId());

        handleOrder(merchantOrderId, dto.getPaymentId(), dto.getStatus());

        notifyCompany(order, dto.getStatus());
    }

    @Override
    public void notifyCompany(Transaction order, String status) {
        Long merchantOrderId = Long.valueOf( order.getMerchantOrderId().split("::")[1]);

        LiterarySocietyOrderResponseDTO responseDTO = new LiterarySocietyOrderResponseDTO(merchantOrderId,status);

        String companyCommonName = order.getCompany().getCommonName();

        try {
            zuulClient.confirmPaymentToLiterarySociety(URI.create(apiGatewayURL + '/' + companyCommonName), responseDTO);
        } catch (RuntimeException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Went wrong when contacting the Literary Society.")));
        }

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ORD", String.format("Order with id %s is done and payment is successfully forwarded to Literary Society", order.getId())));
    }

    @Override
    public Transaction setTransactionStatus(Transaction transaction,TransactionStatus status) {
        transaction.setStatus(status);
        return transactionRepository.save(transaction);
    }


    @Async
    @Override
    @Scheduled(cron = "0 20 0 * * ?")
    public void synchronizeTransactions() {
        //TODO: Implement
//        try{
//            List<LiterarySocietyOrderRequestDTO> transactions = zuulClient.getAllTransactions("Bearer " + jwtTokenService.getToken());
//
//            for(LiterarySocietyOrderRequestDTO dto: transactions){
//                Transaction order = transactionService.findById(dto.getMerchantOrderId());
//                if(order != null && !order.getStatus().equals(TransactionStatus.valueOf(dto.getStatus()))){
//                    handlePayment(dto);
//                }
//            }
//        }catch (RuntimeException e){
//        }
    }

    private void handleOrder(String merchantOrderId, Long paymentId, String status) {
        Transaction transaction = transactionRepository.findTransactionByMerchantOrderIdAndPaymentId(merchantOrderId, paymentId);

        if (transaction == null) {
            return;
        }

        if (status.equals("SUCCESS")) {
            transaction.setStatus(TransactionStatus.COMPLETED);
        } else if (status.equals("ERROR")) {
            transaction.setStatus(TransactionStatus.CANCELED);
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

        transaction.setMerchantOrderId(company.getCommonName() + "::" + orderRequestDTO.getOrderId());
        transaction.setStatus(TransactionStatus.CREATED);
        transaction.setMerchant(merchant);
        transaction.setPaymentMethod(null);
        transaction.setPaymentId(null);

        System.out.println("Transaction - " + transaction);
        return transactionRepository.save(transaction);
    }

    @Async
    public void executeCancelledTransaction(Long transactionId, Instant executionMoment) {
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduler.schedule(createRunnable(transactionId), executionMoment);
    }

    private Runnable createRunnable(final Long transactionId) {
        return () -> {
            Transaction transaction = transactionRepository.findOneById(transactionId);
            if (transaction != null && transaction.getStatus().equals(TransactionStatus.CREATED)) {
                transaction.setStatus(TransactionStatus.CANCELED);
                transactionRepository.save(transaction);
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SRQ",
                        String.format(
                                "Because 15min from creation have passed, rtransaction %s is automatically CANCELED",
                                transaction.getId())));
            }
        };
    }

    @Autowired
    public OrderServiceImpl(LogService logService, TransactionRepository transactionRepository, CompanyService companyService,
                            PaymentMethodRepository paymentMethodRepository, ForwardRequestMapper forwardRequestMapper,
                            DataForPaymentRepository dataForPaymentRepository, ZuulClient zuulClient,UserService userService) {
        this.logService = logService;
        this.transactionRepository = transactionRepository;
        this.companyService = companyService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.forwardRequestMapper = forwardRequestMapper;
        this.dataForPaymentRepository = dataForPaymentRepository;
        this.zuulClient = zuulClient;
        this.userService = userService;
    }
}
