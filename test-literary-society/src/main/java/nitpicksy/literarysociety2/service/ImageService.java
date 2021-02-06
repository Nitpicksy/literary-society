package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String loadImageAsBase64(String fileName);

    Image saveImage(MultipartFile multipartFile, String path, Book book);

}
