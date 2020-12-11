package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.constants.BankConstants;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.repository.CreditCardRepository;
import nitpicksy.bank.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private CreditCardRepository creditCardRepository;

    private HashValueServiceImpl hashValueServiceImpl;

    @Override
    public CreditCard checkCreditCardDate(String pan, String cardHolderName, String expirationDate, String securityCode) throws NoSuchAlgorithmException {
        CreditCard creditCard = creditCardRepository.findByPanAndCardHolderName(hashValueServiceImpl.getHashValue(pan), hashValueServiceImpl.getHashValue(cardHolderName));
        if(creditCard == null){
            throw new InvalidDataException("Invalid Credit Card data. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if(!BCrypt.checkpw(securityCode, creditCard.getSecurityCode())){
            throw new InvalidDataException("Invalid Credit Card data. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if(!checkCreditCardExpirationDate(creditCard.getExpirationDate(),expirationDate)){
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

        return expirationDate.getMonthValue() >= currentDateAndTime.getMonthValue();
    }

    @Autowired
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository,HashValueServiceImpl hashValueServiceImpl) {
        this.creditCardRepository = creditCardRepository;
        this.hashValueServiceImpl = hashValueServiceImpl;
    }
}
