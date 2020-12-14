package nitpicksy.literarysociety.exceptionHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FileNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;

    public FileNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}

