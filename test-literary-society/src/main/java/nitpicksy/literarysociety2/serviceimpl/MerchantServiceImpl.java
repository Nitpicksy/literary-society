package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.client.ZuulClient;
import nitpicksy.literarysociety2.dto.response.MerchantPaymentGatewayResponseDTO;
import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.Merchant;
import nitpicksy.literarysociety2.repository.MerchantRepository;
import nitpicksy.literarysociety2.service.EmailNotificationService;
import nitpicksy.literarysociety2.service.JWTTokenService;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.MerchantService;
import nitpicksy.literarysociety2.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private MerchantRepository merchantRepository;

    private ZuulClient zuulClient;

    private JWTTokenService jwtTokenService;

    private VerificationServiceImpl verificationService;

    private EmailNotificationService emailNotificationService;

    private Environment environment;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    @Override
    public Merchant findByName(String name) {
        return merchantRepository.findByName(name);
    }

    @Override
    public String getPaymentData(Merchant merchant) {
        try{
            ResponseEntity<String> response = zuulClient.getPaymentData("Bearer " + jwtTokenService.getToken(), merchant.getName());
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        }catch (RuntimeException e){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER",
                    "Forwarding request to get payment data has failed"));
        }
        throw new InvalidDataException("Something went wrong.Please try again.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Merchant save(Merchant merchant) {
        Merchant savedMerchant = merchantRepository.save(merchant);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDM",
                String.format("Merchant %s successfully saved",savedMerchant.getId())));
        return savedMerchant;
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

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDM",
                String.format("Merchant %s successfully sign up from IP address %s",savedMerchant.getId(), ipAddressProvider.get())));
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
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER",
                        String.format("Merchant request for registration %s is approved",merchant.getId())));
            } else {
                merchant.setStatus(UserStatus.REJECTED);
                composeAndSendRejectionEmail(merchant.getEmail());
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER",
                        String.format("Merchant request for registration %s is rejected",merchant.getId())));
            }
            return merchantRepository.save(merchant);
        }
        return null;
    }

    @Async
    public void addMerchantToPaymentGateway(Merchant merchant){
        try{
            zuulClient.addMerchant("Bearer " + jwtTokenService.getToken(),merchant.getName());
        }catch (RuntimeException ex){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "MER",
                    "Forwarding request to add merchant has failed"));
        }

    }

    @Scheduled(cron = "0 50 0 * * ?")
    @Async
    @Override
    public void synchronizeMerchants() {
        try{
            List<MerchantPaymentGatewayResponseDTO> merchants = zuulClient.getAllMerchants("Bearer " + jwtTokenService.getToken());

            for(MerchantPaymentGatewayResponseDTO currentMerchant: merchants){
                Merchant merchant = merchantRepository.findByName(currentMerchant.getName());
                if (merchant != null) {
                    merchant.setSupportsPaymentMethods(currentMerchant.isSupportsPaymentMethods());
                    merchantRepository.save(merchant);
                }
            }
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SYNC",
                    "Successfully synchronized merchants."));
        }catch (RuntimeException e){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SYNC",
                    "Forwarding request to synchronize merchants has failed."));
        }
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
        sb.append("You have to support all available payment methods before you start to sell books.");
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
                               EmailNotificationService emailNotificationService,Environment environment,
                               LogService logService, IPAddressProvider ipAddressProvider) {
        this.merchantRepository = merchantRepository;
        this.zuulClient = zuulClient;
        this.jwtTokenService = jwtTokenService;
        this.verificationService = verificationService;
        this.emailNotificationService =emailNotificationService;
        this.environment = environment;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}
