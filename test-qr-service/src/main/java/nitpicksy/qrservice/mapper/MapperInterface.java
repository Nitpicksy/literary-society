package nitpicksy.qrservice.mapper;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public interface MapperInterface<T, U> {
    T toEntity(U dto) throws ParseException, NoSuchAlgorithmException;

    U toDto(T entity);
}

