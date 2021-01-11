package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.response.LiterarySocietyOrderResponseDTO;
import nitpicksy.paymentgateway.enumeration.TransactionStatus;
import nitpicksy.paymentgateway.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class LiterarySocietyOrderMapper implements MapperInterface<Transaction, LiterarySocietyOrderResponseDTO> {

    @Override
    public Transaction toEntity(LiterarySocietyOrderResponseDTO dto) {
        return null;
    }

    @Override
    public LiterarySocietyOrderResponseDTO toDto(Transaction entity) {
        LiterarySocietyOrderResponseDTO dto = new LiterarySocietyOrderResponseDTO();

        Long merchantOrderId = Long.valueOf(entity.getMerchantOrderId().split("::")[1]);

        dto.setMerchantOrderId(merchantOrderId);
        if (entity.getStatus().equals(TransactionStatus.COMPLETED)){
            dto.setStatus("SUCCESS");
        }else if (entity.getStatus().equals(TransactionStatus.REJECTED)){
            dto.setStatus("FAILED");
        }else if (entity.getStatus().equals(TransactionStatus.CANCELED)){
            dto.setStatus("ERROR");
        }else {
            dto.setStatus("CREATED");
        }
        return dto;
    }
}
