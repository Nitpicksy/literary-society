package nitpicksy.paypalservice.mapper;

import nitpicksy.paypalservice.dto.response.TransactionResponseDTO;
import nitpicksy.paypalservice.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements MapperInterface<Transaction, TransactionResponseDTO> {

    private ModelMapper modelMapper;

    @Override
    public Transaction toEntity(TransactionResponseDTO dto) {
        return modelMapper.map(dto, Transaction.class);
    }

    @Override
    public TransactionResponseDTO toDto(Transaction entity) {
        return modelMapper.map(entity, TransactionResponseDTO.class);
    }

    @Autowired
    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
