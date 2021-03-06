package nitpicksy.paymentgateway.serviceimpl;
import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Data;
import nitpicksy.paymentgateway.model.DataForPayment;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.model.Transaction;
import nitpicksy.paymentgateway.repository.CompanyRepository;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.service.DataForPaymentService;
import nitpicksy.paymentgateway.service.DataService;
import nitpicksy.paymentgateway.service.EmailNotificationService;
import nitpicksy.paymentgateway.service.LogService;
import nitpicksy.paymentgateway.service.OrderService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import nitpicksy.paymentgateway.utils.CertificateUtils;
import nitpicksy.paymentgateway.utils.TrustStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private OrderService orderService;

    private PaymentMethodRepository paymentMethodRepository;

    private DataService dataService;

    private EmailNotificationService emailNotificationService;

    private CompanyRepository companyRepository;

    private DataForPaymentService dataForPaymentService;

    private ZuulClient zuulClient;

    private LogService logService;

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    public List<PaymentMethod> findMerchantPaymentMethods(Long orderId) {

        Transaction order = orderService.findOrder(orderId);
        if (order == null) {
            throw new InvalidDataException("Order is invalid or doesn't exist.", HttpStatus.BAD_REQUEST);
        }

        List<PaymentMethod> paymentMethods = new ArrayList<>(order.getCompany().getPaymentMethods());
        if (paymentMethods.isEmpty()) {
            order = orderService.setTransactionStatus(order, TransactionStatus.CANCELED);
            orderService.notifyCompany(order, "ERROR");
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
        if ((paymentMethodRepository.findByName(paymentMethod.getName()) != null) ||
                (paymentMethodRepository.findByCommonName(paymentMethod.getCommonName()) != null)) {
            throw new InvalidDataException("Payment method with the same name already exists.", HttpStatus.BAD_REQUEST);
        }
        if (paymentMethodRepository.findByCertificateName(paymentMethod.getCertificateName()) != null
                || companyRepository.findByCertificateName(paymentMethod.getCertificateName()) != null) {
            throw new InvalidDataException("Certificate name already in use. Please try with another one.", HttpStatus.BAD_REQUEST);
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
    public List<PaymentMethod> findAllApproved() {
        return paymentMethodRepository.findByStatus(PaymentMethodStatus.APPROVED);
    }

    @Override
    public List<PaymentMethod> findByIds(List<Long> ids) {
        return paymentMethodRepository.findByIdIn(ids);
    }

    @Override
    public PaymentMethod changePaymentMethodStatus(Long id, String status) {
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(id);
        if (optionalPaymentMethod.isPresent()) {
            PaymentMethod paymentMethod = optionalPaymentMethod.get();
            if (status.equals("approve")) {
                try {
                    KeyStore  trustStore= TrustStoreUtils.loadKeyStore();
                    X509Certificate certificate = CertificateUtils.getCertificate(paymentMethod.getCertificateName());
                    TrustStoreUtils.importCertificateInTrustStore(certificate, paymentMethod.getCommonName(), trustStore);
                } catch (Exception e) {

                }
                paymentMethod.setStatus(PaymentMethodStatus.APPROVED);
                composeAndSendEmailApprovedRequest(paymentMethod.getEmail());
            } else {
                paymentMethod.setStatus(PaymentMethodStatus.REJECTED);
                composeAndSendRejectionEmail(paymentMethod.getEmail());
            }
            return paymentMethodRepository.save(paymentMethod);
        }
        return null;
    }

    @Override
    public List<PaymentMethod> getPaymentMethodsWithoutDataForPayment(Merchant merchant) {
        Set<PaymentMethod> companyPaymentMethods = merchant.getCompany().getPaymentMethods();
        Set<Long>  companyPaymentMethodsIds = new HashSet<>();
        for (PaymentMethod paymentMethod: companyPaymentMethods){
            companyPaymentMethodsIds.add(paymentMethod.getId());
        }
        List<DataForPayment> dataForPayments = dataForPaymentService.findDataForPaymentByMerchant(merchant.getId());
        Set<Long> supportedPaymentMethods = new HashSet<>();
        for (DataForPayment dataForPayment: dataForPayments){
            supportedPaymentMethods.add(dataForPayment.getPaymentMethod().getId());
        }
        if(supportedPaymentMethods.isEmpty()){
            return  new ArrayList<>(companyPaymentMethods);
        }
        return paymentMethodRepository.findByIdInAndIdNotIn(companyPaymentMethodsIds, supportedPaymentMethods);
    }

    @Override
    public PaymentMethod findById(Long id) {
        return paymentMethodRepository.findById(id).get();
    }

    @Override
    public String changeSupportPaymentMethods(List<PaymentMethod> listPaymentMethods, Company company) {
        boolean supportPaymentMethods = true;
        for (PaymentMethod paymentMethod: listPaymentMethods) {
            PaymentMethod companyPaymentMethod = company.getPaymentMethods().stream()
                    .filter(current -> current.getId().equals(paymentMethod.getId())).findAny().orElse(null);
            if(companyPaymentMethod == null){
                supportPaymentMethods = false;
                break;
            }
        }

        for (PaymentMethod paymentMethod: company.getPaymentMethods()) {
            PaymentMethod supportedPaymentMethod = listPaymentMethods.stream()
                    .filter(current -> current.getId().equals(paymentMethod.getId())).findAny().orElse(null);
            if(supportedPaymentMethod == null){
                dataForPaymentService.deleteCompanyDataForPayment(paymentMethod.getId(), company.getId());
            }
        }
        company.setPaymentMethods(new HashSet<>(listPaymentMethods));
        companyRepository.save(company);
        String redirectURL = null;
        try {
            redirectURL = zuulClient.choosePaymentMethods(URI.create(apiGatewayURL + '/' + company.getCommonName()), supportPaymentMethods);
        }catch (RuntimeException e){
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PAYM", "Could not notify " + company.getCommonName()));
        }
        return redirectURL;
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
                                    DataService dataService, EmailNotificationService emailNotificationService,
                                    CompanyRepository companyRepository, DataForPaymentService dataForPaymentService,
                                    ZuulClient zuulClient,LogService logService) {
        this.orderService = orderService;
        this.paymentMethodRepository = paymentMethodRepository;
        this.dataService = dataService;
        this.emailNotificationService = emailNotificationService;
        this.companyRepository = companyRepository;
        this.dataForPaymentService = dataForPaymentService;
        this.zuulClient = zuulClient;
        this.logService = logService;
    }
}
