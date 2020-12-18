package nitpicksy.paymentgateway.repository;


import nitpicksy.paymentgateway.model.Writable;

import java.io.IOException;
import java.nio.file.Path;


public interface FileRepository<T extends Writable> {

    void write(Path file, T t) throws IOException;

}
