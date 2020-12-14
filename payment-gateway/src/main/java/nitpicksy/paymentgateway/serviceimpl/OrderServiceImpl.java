package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.model.Transaction;
import nitpicksy.paymentgateway.repository.CompanyRepository;
import nitpicksy.paymentgateway.repository.TransactionRepository;
import nitpicksy.paymentgateway.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class OrderServiceImpl implements OrderService {

    private TransactionRepository transactionRepository;
    private CompanyRepository companyRepository;

    @Override
    public Transaction createOrder(OrderRequestDTO orderDTO) {

        //TODO: based on certificate? for now we only have 1 literary society
        Company company = companyRepository.findByCommonName("nitpicksy.com");

        if (company == null) {
            throw new InvalidDataException("Company not found.", HttpStatus.BAD_REQUEST);
        }

        Merchant orderMerchant = company.getMerchant().stream()
                .filter(merchant -> merchant.getName().equals(orderDTO.getMerchantName())).findAny().orElse(null);

        if (orderMerchant == null) {
            throw new InvalidDataException("Merchant not found.", HttpStatus.BAD_REQUEST);
        }
        System.out.println("Merchant " + orderMerchant);

        return createTransaction(orderDTO, orderMerchant, company);
    }


    private Transaction createTransaction(OrderRequestDTO orderRequestDTO, Merchant merchant, Company company) {
        Transaction transaction = new Transaction();
        transaction.setCompany(company);
        transaction.setAmount(orderRequestDTO.getAmount().doubleValue());
        transaction.setMerchantTimestamp(LocalDateTime.ofInstant(orderRequestDTO.getTimestamp().toInstant(), ZoneId.systemDefault()));
        transaction.setMerchantOrderId(orderRequestDTO.getOrderId());
        transaction.setStatus(TransactionStatus.CREATED);
        transaction.setMerchant(merchant);

        System.out.println("Transaction - " + transaction);
        return transactionRepository.save(transaction);
    }

    @Autowired
    public OrderServiceImpl(TransactionRepository transactionRepository, CompanyRepository companyRepository) {
        this.transactionRepository = transactionRepository;
        this.companyRepository = companyRepository;
    }
}
