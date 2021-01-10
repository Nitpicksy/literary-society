package nitpicksy.qrservice.repository;


import nitpicksy.qrservice.model.Writable;

import java.io.IOException;
import java.nio.file.Path;

public interface FileRepository<T extends Writable> {

    void write(Path file, T t) throws IOException;

}
