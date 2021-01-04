package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String loadImageAsBase64(String fileName);

    Image saveImage(MultipartFile multipartFile, String path, Book book);

}
