package nitpicksy.literarysociety.plagiarist.utils;

import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.repository.PDFDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadFile {

    private final String BOOKS_PATH = "literary-society/src/main/resources/books/";

    private PDFDocumentRepository pdfDocumentRepository;

    public File download(PDFDocument pdfDocument) throws IOException {
        Path fileStorageLocation = Paths.get(BOOKS_PATH);
        Path filePath = fileStorageLocation.resolve(pdfDocument.getName()).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        File file = resource.getFile();

        return file;
    }

    public PDFDocument findByBookId(Long id) {
        return pdfDocumentRepository.findByBookIdOrderByCreatedDesc(id).get(0);
    }

    @Autowired
    public UploadFile( PDFDocumentRepository pdfDocumentRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
    }
}
