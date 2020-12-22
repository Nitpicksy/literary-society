package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.PDFDocumentRepository;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PDFDocumentServiceImpl implements PDFDocumentService {

    private final String BOOKS_PATH = "books/";

    private PDFDocumentRepository pdfDocumentRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public byte[] download(String name) throws IOException {
        java.nio.file.Path fileStorageLocation = Paths.get("literary-society/src/main/resources/books/");
        java.nio.file.Path filePath = fileStorageLocation.resolve(name).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        resource.getFilename();
        File file = resource.getFile();

        return Files.readAllBytes(file.toPath());
    }

    @Override
    public PDFDocument findByBookId(Long id) {
        return pdfDocumentRepository.findByBookIdOrderByCreatedDesc(id).get(0);
    }

    @Autowired
    public PDFDocumentServiceImpl(PDFDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }
}
