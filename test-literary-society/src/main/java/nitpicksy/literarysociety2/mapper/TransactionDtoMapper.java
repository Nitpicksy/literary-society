package nitpicksy.literarysociety2.mapper;

import nitpicksy.literarysociety2.dto.response.BookDTO;
import nitpicksy.literarysociety2.dto.response.TransactionDTO;
import nitpicksy.literarysociety2.model.BuyerToken;
import nitpicksy.literarysociety2.model.Transaction;
import nitpicksy.literarysociety2.service.BuyerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionDtoMapper  implements MapperInterface<Transaction, TransactionDTO> {

    private BuyerTokenService buyerTokenService;

    private BookDtoMapper bookDtoMapper;

    private Environment environment;

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
        BuyerToken token =  buyerTokenService.find(entity.getId());
        if(token != null ){
            transactionDTO.setUrl("/books/download?t=" + token.getToken());
        }else{
            transactionDTO.setUrl("/purchased-books");
        }

        return transactionDTO;
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public TransactionDtoMapper(BookDtoMapper bookDtoMapper,BuyerTokenService buyerTokenService,Environment environment) {
        this.buyerTokenService = buyerTokenService;
        this.bookDtoMapper = bookDtoMapper;
        this.environment = environment;
    }
}
