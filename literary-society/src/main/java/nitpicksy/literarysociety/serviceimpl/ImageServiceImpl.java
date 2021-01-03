package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.exceptionHandler.FileNotFoundException;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Image;
import nitpicksy.literarysociety.model.Log;
import nitpicksy.literarysociety.repository.ImageRepository;
import nitpicksy.literarysociety.service.ImageService;
import nitpicksy.literarysociety.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {

    private static String IMAGES_PATH = "literary-society/src/main/resources/images/";

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private LogService logService;

    private ImageRepository imageRepository;

    @Override
    public String loadImageAsBase64(String fileName) {
        try {
            Path fileStorageLocation = Paths.get(IMAGES_PATH);
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            File file = resource.getFile();
//
//            File resource = new ClassPathResource(IMAGES_PATH + fileName).getFile();
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            String encodedImage = Base64.getEncoder().encodeToString(fileContent);
            return String.format("data:image/%s;base64,%s", extension, encodedImage);
        } catch (IOException e) {
            logService.write(new Log(Log.WARN, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PEX",
                    String.format("Image \"%s\" not found on server", fileName)));
            throw new FileNotFoundException("File " + fileName + " not found.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
    public Image saveImage(MultipartFile multipartFile, String path, Book book) {
        Image image = imageRepository
                .saveAndFlush(new Image(saveOnDisk(multipartFile, path, book.getId())));
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PDB",
                String.format("Picture %s successfully saved in DB", image.getId())));
        return  image;
    }

    private String saveOnDisk(MultipartFile image, String path, Long id) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                throw new InvalidDataException(fileName + " - file name contains invalid path sequence.",
                        HttpStatus.BAD_REQUEST);
            }
            Path fileStorageLocation = Paths.get(path);
            Path targetLocation = fileStorageLocation.resolve(id + "_" + fileName);
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PDI",
                    String.format("Image \"%s_%s\" successfully saved on server", id, fileName)));
            return id + "_" + fileName;
        } catch (IOException ex) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PEX",
                    String.format("Image \"%s_%s\" could not be stored on server", id, fileName)));
            throw new InvalidDataException("Could not store file " + fileName + ". Please try again!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public ImageServiceImpl(LogService logService, ImageRepository imageRepository) {
        this.logService = logService;
        this.imageRepository = imageRepository;
    }
}
