package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.model.User;

import java.security.NoSuchAlgorithmException;

public interface ReaderService {

    Reader create(Reader reader) throws NoSuchAlgorithmException;
}
