package nitpicksy.literarysociety.mapper;

import nitpicksy.literarysociety.dto.response.BookDTO;
import nitpicksy.literarysociety.dto.response.TransactionDTO;
import nitpicksy.literarysociety.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionDtoMapper  implements MapperInterface<Transaction, TransactionDTO> {

    private ModelMapper modelMapper;

    private BookDtoMapper bookDtoMapper;

    @Override
    public Transaction toEntity(TransactionDTO dto) {
        return null;
    }

    @Override
    public TransactionDTO toDto(Transaction entity) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(entity.getId());
        transactionDTO.setAmount(entity.getAmount());
        List<BookDTO> bookDTOS = entity.getOrderedBooks().stream().map(bookDtoMapper::toDto).collect(Collectors.toList());
        transactionDTO.setOrderedBooks(bookDTOS);
        return transactionDTO;
    }

    @Autowired
    public TransactionDtoMapper(ModelMapper modelMapper,BookDtoMapper bookDtoMapper) {
        this.modelMapper = modelMapper;
        this.bookDtoMapper = bookDtoMapper;
    }
}
