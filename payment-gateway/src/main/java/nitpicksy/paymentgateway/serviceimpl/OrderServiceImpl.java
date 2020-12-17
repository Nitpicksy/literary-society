package nitpicksy.paymentgateway.serviceimpl;

import feign.FeignException;
import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.response.LiterarySocietyOrderResponseDTO;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.mapper.ForwardRequestMapper;
import nitpicksy.paymentgateway.model.*;
import nitpicksy.paymentgateway.repository.DataForPaymentRepository;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.repository.TransactionRepository;
import nitpicksy.paymentgateway.service.CompanyService;
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

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    private TransactionRepository transactionRepository;
    private CompanyService companyService;
    private PaymentMethodRepository paymentMethodRepository;
    private ForwardRequestMapper forwardRequestMapper;
    private DataForPaymentRepository dataForPaymentRepository;
    private ZuulClient zuulClient;

    @Override
    public Transaction createOrder(OrderRequestDTO orderDTO) {

        //TODO: based on certificate? for now we only have 1 literary society
        Company company = companyService.findCompanyByCommonName("literary-society");

        if (company == null) {
            throw new InvalidDataException("Company not found.", HttpStatus.BAD_REQUEST);
        }

        Merchant orderMerchant = company.getMerchant().stream()
                .filter(merchant -> merchant.getName().equals(orderDTO.getMerchantName())).findAny().orElse(null);

        if (orderMerchant == null) {
            throw new InvalidDataException("Merchant not found.", HttpStatus.BAD_REQUEST);
        }

        return createTransaction(orderDTO, orderMerchant, company);
    }

    @Override
    public DynamicPaymentDetailsDTO forwardPaymentRequest(Long orderId, String paymentMethodCommonName) {

        Transaction transaction = findOrder(orderId);
        if (transaction == null) {
            throw new InvalidDataException("Invalid Transaction.", HttpStatus.BAD_REQUEST);
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findByCommonName(paymentMethodCommonName);
        if (paymentMethod == null) {
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

        return forwardDTO;
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
            throw new InvalidDataException("Transaction null when receiving invoice from bank.", HttpStatus.BAD_REQUEST);
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
            System.out.println("Went wrong with contacting the Literary Society.");
        }
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
    public OrderServiceImpl(TransactionRepository transactionRepository, CompanyService companyService, PaymentMethodRepository paymentMethodRepository, ForwardRequestMapper forwardRequestMapper, DataForPaymentRepository dataForPaymentRepository, ZuulClient zuulClient) {
        this.transactionRepository = transactionRepository;
        this.companyService = companyService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.forwardRequestMapper = forwardRequestMapper;
        this.dataForPaymentRepository = dataForPaymentRepository;
        this.zuulClient = zuulClient;
    }
}
