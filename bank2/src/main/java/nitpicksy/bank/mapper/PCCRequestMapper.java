package nitpicksy.bank.mapper;

import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.request.PCCRequestDTO;
import nitpicksy.bank.model.Transaction;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import org.joda.time.DateTime;

@Component
public class PCCRequestMapper {

    public PCCRequestDTO toDTO(Transaction transaction , ConfirmPaymentDTO confirmPaymentDTO) {
        ConfirmPaymentDTO confirmPaymentDTOWithHashedValues = new ConfirmPaymentDTO(confirmPaymentDTO.getPAN(),confirmPaymentDTO.getCardHolderName(),
                confirmPaymentDTO.getExpirationDate(), confirmPaymentDTO.getSecurityCode());
        System.out.println(confirmPaymentDTOWithHashedValues.getSecurityCode());
        return new PCCRequestDTO(transaction.getId(), new Timestamp(DateTime.now().getMillis()),
                transaction.getAmount(), confirmPaymentDTOWithHashedValues,getBankIdentificationNumber(confirmPaymentDTO.getPAN()),
                transaction.getMerchantId(),transaction.getMerchantOrderId(),
                transaction.getMerchantTimestamp(), transaction.getPaymentId());
    }

    public String getBankIdentificationNumber(String pan){
        String bankIdentificationNumber = pan.substring(1, 7);
        return bankIdentificationNumber;
    }
}
