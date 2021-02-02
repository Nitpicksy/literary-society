package nitpicksy.literarysociety2.dto.camunda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlagiarismBooksDTO {
    
    byte[] submitted;

    byte[] reported;
}
