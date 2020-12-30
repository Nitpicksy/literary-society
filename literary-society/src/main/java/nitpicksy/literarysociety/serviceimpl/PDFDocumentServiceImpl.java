package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.camunda.WriterDocumentDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.repository.PDFDocumentRepository;
import nitpicksy.literarysociety.repository.WriterRepository;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
public class PDFDocumentServiceImpl implements PDFDocumentService {

    private final String BOOKS_PATH = "literary-society/src/main/resources/books/";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private PDFDocumentRepository pdfDocumentRepository;
    private WriterRepository writerRepository;

    @Override
    public PDFDocument upload(MultipartFile pdfFile, Book book) throws IOException {
        if (!pdfFile.isEmpty()) {
            byte[] bytes = pdfFile.getBytes();

            LocalDateTime created = LocalDateTime.now();
            String pdfName = created.format(FORMATTER) + "_" + pdfFile.getOriginalFilename();

            PDFDocument newPdfDoc = new PDFDocument(pdfName, created, book);
            pdfDocumentRepository.save(newPdfDoc);

            Path path = Paths.get(BOOKS_PATH + File.separator + pdfName);
            Files.write(path, bytes);

            return pdfDocumentRepository.findByName(pdfName);
        }
        return null;
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

    @Override
    public List<WriterDocumentDTO> getDraftsByWriter(String writerUsername) {

        List<WriterDocumentDTO> dtoList = new ArrayList<>();
        Writer writer = writerRepository.findByUsername(writerUsername);

        if (writer == null) {
            return null;
        }

        Set<PDFDocument> drafts = writer.getDrafts();

        for (PDFDocument draft : drafts) {
            try {
                String encoded = Base64.getEncoder().encodeToString(download(draft.getName()));
                dtoList.add(new WriterDocumentDTO(encoded,
                        draft.getName(),
                        writer.getAttempts(),
                        writer.getFirstName(),
                        writer.getLastName(),
                        writer.getUsername()));
            } catch (IOException e) {
                throw new InvalidDataException("Unknown exception when reading given draft", HttpStatus.BAD_REQUEST);
            }

        }

        return dtoList;
    }

    @Autowired
    public PDFDocumentServiceImpl(PDFDocumentRepository pdfDocumentRepository, WriterRepository writerRepository) {
        this.pdfDocumentRepository = pdfDocumentRepository;
        this.writerRepository = writerRepository;
    }
}
