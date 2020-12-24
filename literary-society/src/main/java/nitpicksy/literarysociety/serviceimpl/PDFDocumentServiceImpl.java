package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.repository.PDFDocumentRepository;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFDocumentServiceImpl implements PDFDocumentService {

    private final String BOOKS_PATH = "literary-society/src/main/resources/books/";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private PDFDocumentRepository pdfDocumentRepository;

    @Override
    public void upload(MultipartFile pdfFile, Book book) throws IOException {
        if (!pdfFile.isEmpty()) {
            byte[] bytes = pdfFile.getBytes();

            LocalDateTime created = LocalDateTime.now();
            String pdfName = created.format(FORMATTER) + "_" + pdfFile.getOriginalFilename();

            PDFDocument newPdfDoc = new PDFDocument(pdfName, created, book);
            pdfDocumentRepository.save(newPdfDoc);

            Path path = Paths.get(BOOKS_PATH + File.separator + pdfName);
            Files.write(path, bytes);
        }
    }

    @Override
    public byte[] download(String name) throws IOException {
        Path fileStorageLocation = Paths.get(BOOKS_PATH);
        Path filePath = fileStorageLocation.resolve(name).normalize();
        Resource resource = new UrlResource(filePath.toUri());
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
