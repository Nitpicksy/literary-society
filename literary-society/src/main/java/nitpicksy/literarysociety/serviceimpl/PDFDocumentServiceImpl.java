package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.repository.PDFDocumentRepository;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PDFDocumentServiceImpl implements PDFDocumentService {

    private final String BOOKS_PATH = "literary-society/src/main/resources/books/";

    private PDFDocumentRepository pdfDocumentRepository;

    @Override
    public PDFDocument save(PDFDocument pdfDocument) {
        return pdfDocumentRepository.save(pdfDocument);
    }

    @Override
    public byte[] download(String name) throws IOException {
        Path fileStorageLocation = Paths.get(BOOKS_PATH);
        Path filePath = fileStorageLocation.resolve(name).normalize();
        Resource resource = new UrlResource(filePath.toUri());
//        resource.getFilename();
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
