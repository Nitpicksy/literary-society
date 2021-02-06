package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.Writable;

import java.io.IOException;
import java.nio.file.Path;

public interface FileRepository<T extends Writable> {

    void write(Path file, T t) throws IOException;

}
