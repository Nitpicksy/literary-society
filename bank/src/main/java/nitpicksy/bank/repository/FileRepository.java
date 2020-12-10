package nitpicksy.bank.repository;

import nitpicksy.bank.model.Writable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Path;


public interface FileRepository<T extends Writable> {

    void write(Path file, T t) throws IOException;

}
