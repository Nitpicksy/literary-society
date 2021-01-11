package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.constants.BankConstants;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Log;
import nitpicksy.bank.repository.CreditCardRepository;
import nitpicksy.bank.service.CreditCardService;
import nitpicksy.bank.service.LogService;
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


    private LogService logService;

    @Override
    public CreditCard checkCreditCardDate(String pan, String cardHolderName, String expirationDate, String securityCode)  {
        CreditCard creditCard = creditCardRepository.findByPanAndCardHolderNameAndSecurityCode(pan, cardHolderName,securityCode);
        if(creditCard == null){
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
    public boolean isClientOfThisBank(String pan){
        String bankIdentificationNumber = pan.substring(1, 7);
        return bankIdentificationNumber.equals(BankConstants.BANK_IDENTIFICATION_NUMBER);
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
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository,LogService logService) {
        this.creditCardRepository = creditCardRepository;
        this.logService = logService;
    }
}
