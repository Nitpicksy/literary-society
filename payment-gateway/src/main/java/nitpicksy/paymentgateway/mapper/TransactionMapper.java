package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.response.OrderDetailsDTO;
import nitpicksy.paymentgateway.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper implements MapperInterface<Transaction, OrderDetailsDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Transaction toEntity(OrderDetailsDTO dto) {
        return null;
    }

    @Override
    public OrderDetailsDTO toDto(Transaction entity) {
        return new OrderDetailsDTO(entity.getMerchant().getName(),
                entity.getCompany().getCompanyName(),
                entity.getAmount());
    }

    @Autowired
    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
