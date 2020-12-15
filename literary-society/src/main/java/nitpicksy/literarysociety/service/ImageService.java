package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Book;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String loadImageAsBase64(String fileName);

    void saveImage(MultipartFile multipartFile, String path, Book book);

}
