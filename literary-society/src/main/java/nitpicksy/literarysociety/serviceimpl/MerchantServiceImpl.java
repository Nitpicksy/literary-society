package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.MerchantRepository;
import nitpicksy.literarysociety.service.EmailNotificationService;
import nitpicksy.literarysociety.service.JWTTokenService;
import nitpicksy.literarysociety.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {

    private MerchantRepository merchantRepository;

    private ZuulClient zuulClient;

    private JWTTokenService jwtTokenService;

    private VerificationServiceImpl verificationService;

    private EmailNotificationService emailNotificationService;

    private Environment environment;

    @Override
    public Merchant findByName(String name) {
        return merchantRepository.findByName(name);
    }

    @Override
    public String getPaymentData(Merchant merchant) {
        ResponseEntity<String> response = zuulClient.getPaymentData("Bearer " + jwtTokenService.getToken(), merchant.getName());
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new InvalidDataException("Something went wrong.Please try again.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Override
    public Merchant findOurMerchant() {
        return merchantRepository.findFirstByOrderByIdAsc();
    }

    @Override
    public Merchant signUp(Merchant merchant) throws NoSuchAlgorithmException {

        if (merchantRepository.findByName(merchant.getName()) != null) {
            throw new InvalidDataException("Merchant with same name already exist",HttpStatus.BAD_REQUEST);
        }

        merchant.setStatus(UserStatus.NON_VERIFIED);
        Merchant savedMerchant = merchantRepository.save(merchant);

        String nonHashedToken = verificationService.generateToken(savedMerchant);
        composeEmailToActivate(nonHashedToken,savedMerchant.getEmail());

        return savedMerchant;
    }

    @Override
    public List<Merchant> findByStatusIn(Collection<UserStatus> status) {
        return merchantRepository.findByStatusIn(status);
    }

    @Override
    public Merchant changeUserStatus(Long id, String status) {
        Optional<Merchant> optionalMerchant = merchantRepository.findById(id);
        if (optionalMerchant.isPresent()) {
            Merchant merchant = optionalMerchant.get();
            if (status.equals("approve")) {
                merchant.setStatus(UserStatus.ACTIVE);
                composeAndSendEmailApprovedRequest(merchant.getEmail());
                addMerchantToPaymentGateway(merchant);
            } else {
                merchant.setStatus(UserStatus.REJECTED);
                composeAndSendRejectionEmail(merchant.getEmail());
            }
            return merchantRepository.save(merchant);
        }
        return null;
    }

    @Async
    public void addMerchantToPaymentGateway(Merchant merchant){
        zuulClient.addMerchant("Bearer " + jwtTokenService.getToken(),merchant.getName());
    }

    private void composeAndSendRejectionEmail(String recipientEmail) {
        String subject = "Request to register rejected";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to register is rejected by a Literary Society administrator.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private void composeAndSendEmailApprovedRequest(String recipientEmail) {
        String subject = "Request to register accepted";
        StringBuilder sb = new StringBuilder();
        sb.append("Your request to register is accepted by a Literary Society administrator.");
        sb.append(System.lineSeparator());
        String text = sb.toString();
        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }


    private void composeEmailToActivate(String token, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("You have successfully registered to the Literary Society website.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("To activate your account click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append(String.format("activate-account?t=%s", token));
        sb.append(System.lineSeparator());
        sb.append("When you activate your account you will be notified when the admins approves your request.");
        emailNotificationService.sendEmail(email, "Activate account", sb.toString());
    }

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository, ZuulClient zuulClient, JWTTokenService jwtTokenService,VerificationServiceImpl verificationService,
                               EmailNotificationService emailNotificationService,Environment environment) {
        this.merchantRepository = merchantRepository;
        this.zuulClient = zuulClient;
        this.jwtTokenService = jwtTokenService;
        this.verificationService = verificationService;
        this.emailNotificationService =emailNotificationService;
        this.environment = environment;
    }
}
