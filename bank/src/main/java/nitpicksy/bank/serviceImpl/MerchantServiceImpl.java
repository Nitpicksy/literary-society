package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.repository.MerchantRepository;
import nitpicksy.bank.service.EmailNotificationService;
import nitpicksy.bank.service.MerchantService;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
public class MerchantServiceImpl implements MerchantService {

    private MerchantRepository merchantRepository;

    private HashValueServiceImpl hashValueServiceImpl;

    private EmailNotificationService emailNotificationService;

    @Override
    public Merchant findByMerchantIdAndPassword(String merchantId, String merchantPassword) {
        Merchant merchant = merchantRepository.findByMerchantId(merchantId);

        if(merchant == null){
            throw new InvalidDataException("Invalid merchant id or password. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if(!merchantPassword.equals(merchant.getMerchantPassword())){
            throw new InvalidDataException("Invalid merchant id or password. Please try again.", HttpStatus.BAD_REQUEST);
        }
        return merchant;
    }

    @Override
    public void transferMoneyToMerchant(String merchantId, Double amount) throws NoSuchAlgorithmException {
        Merchant merchant = merchantRepository.findByMerchantId(merchantId);
        merchant.setBalance(merchant.getBalance() + amount);
        merchantRepository.save(merchant);
    }

    @Override
    public List<Merchant> findAll() {
        return merchantRepository.findAll();
    }

    @Override
    public Merchant save(Merchant merchant) throws NoSuchAlgorithmException {
        String merchantId = generateMerchantId();

        System.out.println(merchantId);
        while(merchantRepository.findByMerchantId(hashValueServiceImpl.getHashValue(merchantId)) != null){
            System.out.println("Genrate new value");
            merchantId = generateMerchantId();
        }
        merchant.setMerchantId(hashValueServiceImpl.getHashValue(merchantId));
        merchant.setMerchantPassword(hashValueServiceImpl.bcryptHash(merchant.getMerchantPassword()));
        composeAndSendEmail(merchant.getEmail(),merchantId);
        return merchantRepository.save(merchant);
    }

    private String generateMerchantId(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9').build();
        return generator.generate(16);
    }

    private void composeAndSendEmail(String recipientEmail, String merchantId) {
        String subject = "Crate merchant account";
        StringBuilder sb = new StringBuilder();
        sb.append("An account for you on Bank website has been created.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Your merchantId is: ");
        sb.append(merchantId);
        sb.append(System.lineSeparator());
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }
    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository, HashValueServiceImpl hashValueServiceImpl,EmailNotificationService emailNotificationService) {
        this.merchantRepository = merchantRepository;
        this.hashValueServiceImpl = hashValueServiceImpl;
        this.emailNotificationService = emailNotificationService;
    }
}
