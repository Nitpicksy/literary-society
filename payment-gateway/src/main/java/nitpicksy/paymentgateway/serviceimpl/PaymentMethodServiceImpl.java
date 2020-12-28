package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.Data;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.model.Transaction;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.service.DataService;
import nitpicksy.paymentgateway.service.EmailNotificationService;
import nitpicksy.paymentgateway.service.OrderService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private OrderService orderService;

    private PaymentMethodRepository paymentMethodRepository;

    private DataService dataService;

    private EmailNotificationService emailNotificationService;

    public List<PaymentMethod> findMerchantPaymentMethods(Long orderId) {

        Transaction order = orderService.findOrder(orderId);
        if (order == null) {
            throw new InvalidDataException("Order is invalid or doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        List<PaymentMethod> paymentMethods = new ArrayList<>(order.getCompany().getPaymentMethods());
        if (paymentMethods.isEmpty()) {
            throw new InvalidDataException("No payment methods registered to order.", HttpStatus.BAD_REQUEST);
        }

        return paymentMethods;
    }

    @Override
    public PaymentMethod findPaymentMethod(String commonName) {
        return paymentMethodRepository.findByCommonName(commonName);
    }

    @Override
    public PaymentMethod registerPaymentMethod(PaymentMethod paymentMethod, Set<Data> listData) {
        if((paymentMethodRepository.findByName(paymentMethod.getName())!= null) ||
                (paymentMethodRepository.findByCommonName(paymentMethod.getCommonName()) != null)){
            throw new InvalidDataException("Payment method with same name already exist", HttpStatus.BAD_REQUEST);
        }
        Set<Data> createdData = dataService.create(listData);
        paymentMethod.setData(createdData);

        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findByStatusNot(PaymentMethodStatus.REJECTED);
    }

    @Override
    public PaymentMethod changePaymentMethodStatus(Long id, String status) {
        Optional<PaymentMethod> optionalPaymentMethod =  paymentMethodRepository.findById(id);
        if(optionalPaymentMethod.isPresent()){
            PaymentMethod paymentMethod = optionalPaymentMethod.get();
            if(status.equals("approve")){
                //add certificate in truststore
                paymentMethod.setStatus(PaymentMethodStatus.APPROVED);
                composeAndSendEmailApprovedRequest(paymentMethod.getEmail());
            }else {
                paymentMethod.setStatus(PaymentMethodStatus.REJECTED);
                composeAndSendRejectionEmail(paymentMethod.getEmail());
            }
            return paymentMethodRepository.save(paymentMethod);
        }
        return null;
    }

    private void composeAndSendRejectionEmail(String recipientEmail) {
        String subject = "Request to register rejected";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to register is rejected by a payment gateway administrator.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private void composeAndSendEmailApprovedRequest(String recipientEmail) {
        String subject = "Request to register accepted";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to register is accepted by a payment gateway administrator.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    @Autowired
    public PaymentMethodServiceImpl(OrderService orderService, PaymentMethodRepository paymentMethodRepository,
                                    DataService dataService,EmailNotificationService emailNotificationService) {
        this.orderService = orderService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.dataService = dataService;
        this.emailNotificationService = emailNotificationService;
    }
}
