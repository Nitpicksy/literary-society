package nitpicksy.literarysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailDTO {

    private Long id;
    private String transactionStatus;
    private String transactionType;
    private String buyerName;
    private String timestamp;
    private Double amount;
    private List<BookDTO> orderedBooks;
    private String merchant;

}
