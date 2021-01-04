package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.constants.BankConstants;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.Account;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Log;
import nitpicksy.bank.repository.CreditCardRepository;
import nitpicksy.bank.service.CreditCardService;
import nitpicksy.bank.service.EmailNotificationService;
import nitpicksy.bank.service.LogService;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private CreditCardRepository creditCardRepository;

    private HashValueServiceImpl hashValueServiceImpl;

    private LogService logService;

    private EmailNotificationService emailNotificationService;

    @Override
    public CreditCard checkCreditCardDate(String pan, String cardHolderName, String expirationDate, String securityCode) throws NoSuchAlgorithmException {
        CreditCard creditCard = creditCardRepository.findByPanAndCardHolderName(hashValueServiceImpl.getHashValue(pan), hashValueServiceImpl.getHashValue(cardHolderName));
        if(creditCard == null){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CARD", String.format("Invalid Credit Card data.")));
            throw new InvalidDataException("Invalid Credit Card data. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if(!BCrypt.checkpw(securityCode, creditCard.getSecurityCode())){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CARD", String.format("Invalid Credit Card data.")));
            throw new InvalidDataException("Invalid Credit Card data. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if(!checkCreditCardExpirationDate(creditCard.getExpirationDate(),expirationDate)){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CARD", String.format("Invalid Credit Card data.")));
            throw new InvalidDataException("Invalid Credit Card data. Please try again.", HttpStatus.BAD_REQUEST);
        }

        return creditCard;
    }

    @Override
    public CreditCard checkCreditCardDateHashedValues(String pan, String cardHolderName, String expirationDate, String securityCode){
        CreditCard creditCard = creditCardRepository.findByPanAndCardHolderName(pan, cardHolderName);
        if(creditCard == null){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CARD", String.format("Invalid Credit Card data.")));
            return null;
        }

        if(!BCrypt.checkpw(securityCode, creditCard.getSecurityCode())){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CARD", String.format("Invalid Credit Card data.")));
            return null;
        }

        if(!checkCreditCardExpirationDate(creditCard.getExpirationDate(),expirationDate)){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CARD", String.format("Invalid Credit Card data.")));
            return null;
        }

        return creditCard;
    }
    
    @Override
    public boolean isClientOfThisBank(String pan){
        String bankIdentificationNumber = pan.substring(1, 7);
        return bankIdentificationNumber.equals(BankConstants.BANK_IDENTIFICATION_NUMBER);
    }

    @Override
    public CreditCard create(Account account) throws NoSuchAlgorithmException {
        CreditCard creditCard = new CreditCard();

        String pan = generatePan();
        creditCard.setPan(hashValueServiceImpl.getHashValue(pan));

        String securityCode = generateSecurityCode();
        creditCard.setSecurityCode(hashValueServiceImpl.bcryptHash(securityCode));

        String cardHolderName = account.getFirstName().toUpperCase() + " " + account.getLastName().toUpperCase();
        creditCard.setCardHolderName(hashValueServiceImpl.getHashValue(cardHolderName));

        creditCard.setExpirationDate(LocalDate.now().plusYears(5));

        composeAndSendEmail(account.getEmail(),pan,securityCode,cardHolderName);

        return creditCardRepository.saveAndFlush(creditCard);
    }

    private String generatePan(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9').build();
        return generator.generate(1) + BankConstants.BANK_IDENTIFICATION_NUMBER + generator.generate(9);
    }

    private String generateSecurityCode(){
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9').build();
        return generator.generate(3);
    }

    private void composeAndSendEmail(String recipientEmail, String pan, String securityCode, String cardHolderName) {
        String subject = "Crate client account";
        StringBuilder sb = new StringBuilder();
        sb.append("An account for you on Bank website has been created.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("Your pan number is: ");
        sb.append(pan);
        sb.append(System.lineSeparator());
        sb.append("Your security code is: ");
        sb.append(securityCode);
        sb.append(System.lineSeparator());
        sb.append("Your cardHolder name is: ");
        sb.append(cardHolderName);
        sb.append(System.lineSeparator());
        String text = sb.toString();

        emailNotificationService.sendEmail(recipientEmail, subject, text);
    }

    private boolean checkCreditCardExpirationDate(LocalDate expirationDate, String expiration){
        String[] values = expiration.split("/");
        int month = Integer.parseInt(values[0]);

        if(month != expirationDate.getMonthValue()){
            return false;
        }
        String yearFromDatabase = Integer.toString(expirationDate.getYear()).substring(2);

        if(!values[1].equals(yearFromDatabase)){
            return false;
        }
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        if(expirationDate.getYear() < currentDateAndTime.getYear()){
            return false;
        }
        if(expirationDate.getYear() == currentDateAndTime.getYear()){
            return expirationDate.getMonthValue() >= currentDateAndTime.getMonthValue();
        }
        return true;
    }

    @Autowired
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository,HashValueServiceImpl hashValueServiceImpl,LogService logService,
                                 EmailNotificationService emailNotificationService) {
        this.creditCardRepository = creditCardRepository;
        this.hashValueServiceImpl = hashValueServiceImpl;
        this.logService = logService;
        this.emailNotificationService = emailNotificationService;
    }
}
