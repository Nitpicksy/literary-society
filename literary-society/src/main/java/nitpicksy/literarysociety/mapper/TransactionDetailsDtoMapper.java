package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.BookDTO;
import nitpicksy.literarysociety.dto.response.TransactionDetailDTO;
import nitpicksy.literarysociety.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionDetailsDtoMapper implements MapperInterface<Transaction, TransactionDetailDTO> {

    private BookDtoMapper bookDtoMapper;

    @Override
    public Transaction toEntity(TransactionDetailDTO dto) {
        return null;
    }

    @Override
    public TransactionDetailDTO toDto(Transaction entity) {
        TransactionDetailDTO transactionDTO = new TransactionDetailDTO();
        transactionDTO.setId(entity.getId());
        transactionDTO.setAmount(entity.getAmount());
        List<BookDTO> bookDTOS = entity.getOrderedBooks().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        transactionDTO.setOrderedBooks(bookDTOS);
        transactionDTO.setTransactionType(entity.getType().toString());
        transactionDTO.setTransactionStatus(entity.getStatus().toString());
        transactionDTO.setBuyerName(entity.getBuyer().getFirstName() + " " + entity.getBuyer().getLastName());
        transactionDTO.setMerchant(entity.getMerchant().getName());
        transactionDTO.setTimestamp(entity.getMerchantTimestamp().toString());
        return transactionDTO;
    }

    @Autowired
    public TransactionDetailsDtoMapper(BookDtoMapper bookDtoMapper) {
        this.bookDtoMapper = bookDtoMapper;
    }
}
