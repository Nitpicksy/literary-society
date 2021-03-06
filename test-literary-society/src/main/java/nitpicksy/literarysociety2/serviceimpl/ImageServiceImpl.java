package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.exceptionHandler.FileNotFoundException;
import nitpicksy.literarysociety2.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Image;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.repository.ImageRepository;
import nitpicksy.literarysociety2.service.ImageService;
import nitpicksy.literarysociety2.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static String DEFAULT_IMG = "default-img.jpg";

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private LogService logService;

    private ImageRepository imageRepository;

    @Override
    public String loadImageAsBase64(String fileName) {
        try {
            Path fileStorageLocation = Paths.get(IMAGES_PATH);
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            if (!Files.exists(filePath)) {
                filePath = fileStorageLocation.resolve(DEFAULT_IMG).normalize();
            }
            Resource resource = new UrlResource(filePath.toUri());
            File file = resource.getFile();
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
                String.format("Image %s successfully saved in DB", image.getId())));
        return image;
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
