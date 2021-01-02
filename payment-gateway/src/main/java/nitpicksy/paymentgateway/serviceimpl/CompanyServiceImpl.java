package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.enumeration.CompanyStatus;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.model.Role;
import nitpicksy.paymentgateway.repository.CompanyRepository;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.repository.RoleRepository;
import nitpicksy.paymentgateway.security.TokenUtils;
import nitpicksy.paymentgateway.service.CompanyService;
import nitpicksy.paymentgateway.service.EmailNotificationService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import nitpicksy.paymentgateway.utils.CertificateUtils;
import nitpicksy.paymentgateway.utils.TrustStoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    private CompanyRepository companyRepository;

    private PaymentMethodRepository paymentMethodRepository;

    private EmailNotificationService emailNotificationService;

    private ZuulClient zuulClient;

    private RoleRepository roleRepository;

    public TokenUtils tokenUtils;

    @Override
    public Company addCompany(Company company, List<PaymentMethodDTO> paymentMethodDTOs) {
        if (companyRepository.findByCompanyName(company.getCompanyName()) != null
                || companyRepository.findByCommonName(company.getCommonName()) != null) {
            throw new InvalidDataException("Company with the same name already exists.", HttpStatus.BAD_REQUEST);
        }
        if (companyRepository.findByCertificateName(company.getCertificateName()) != null
                || paymentMethodRepository.findByCertificateName(company.getCertificateName()) != null) {
            throw new InvalidDataException("Certificate name already in use. Please try with another one.", HttpStatus.BAD_REQUEST);
        }

        List<Long> ids = paymentMethodDTOs.stream().map(PaymentMethodDTO::getId).collect(Collectors.toList());
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByIdIn(ids);
        if (paymentMethods.isEmpty()) {
            throw new InvalidDataException("Non-existing payment methods chosen", HttpStatus.BAD_REQUEST);
        }

        company.setStatus(CompanyStatus.WAITING_APPROVAL);
        company.setPaymentMethods(new HashSet<>(paymentMethods));
        company.setRole(roleRepository.findByName("ROLE_COMPANY"));

        return companyRepository.save(company);
    }

    @Override
    public Company changeStatus(Long id, String status) {
        Company company = companyRepository.findOneById(id);
        if (company != null) {
            if (status.equals("approve")) {
                try {
                    KeyStore trustStore = TrustStoreUtils.loadKeyStore();
                    X509Certificate certificate = CertificateUtils.getCertificate(company.getCertificateName());
                    TrustStoreUtils.importCertificateInTrustStore(certificate, company.getCommonName(), trustStore);
                } catch (Exception e) {
                }
                String jwtToken = tokenUtils.generateToken(company.getCommonName(), company.getRole().getName(),
                        company.getRole().getPermissions());
                zuulClient.sendJWTToken(URI.create(apiGatewayURL + '/' + company.getCommonName()), jwtToken);
                company.setStatus(CompanyStatus.APPROVED);
                composeAndSendApprovalEmail(company.getEmail());
            } else {
                company.setStatus(CompanyStatus.REJECTED);
                composeAndSendRejectionEmail(company.getEmail());
            }
            return companyRepository.save(company);
        }
        return null;
    }

    @Override
    public String getToken(){
        Company company = companyRepository.findOneById(1L);
        String jwtToken = tokenUtils.generateToken(company.getCommonName(), company.getRole().getName(),
                company.getRole().getPermissions());
        return jwtToken;
    }
    @Override
    public List<Company> findAll() {
        return companyRepository.findByStatusNot(CompanyStatus.REJECTED);
    }

    @Override
    public Company findCompanyByCommonName(String commonName) {
        return companyRepository.findByCommonName(commonName);
    }

    private void composeAndSendRejectionEmail(String recipientEmail) {
        String subject = "Request rejected";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to add your company to the Payment Gateway is rejected.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private void composeAndSendApprovalEmail(String recipientEmail) {
        String subject = "Request approved";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to add your company to the Payment Gateway is approved. Your merchants can now pay via supported payment methods.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, PaymentMethodRepository paymentMethodRepository,
                              EmailNotificationService emailNotificationService, ZuulClient zuulClient,
                              TokenUtils tokenUtils, RoleRepository roleRepository) {
        this.companyRepository = companyRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.emailNotificationService = emailNotificationService;
        this.zuulClient = zuulClient;
        this.tokenUtils = tokenUtils;
        this.roleRepository = roleRepository;
    }
}
