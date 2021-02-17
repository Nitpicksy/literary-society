package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.camunda.WriterDocumentDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public interface PDFDocumentService {

    PDFDocument upload(MultipartFile pdfFile, Book book) throws IOException;

    byte[] download(String name) throws IOException, URISyntaxException;

    PDFDocument findByBookId(Long id);

    List<PDFDocument> findByBooks(Set<Book> books);

    List<WriterDocumentDTO> getDraftsByWriter(String writer);

    File download(PDFDocument pdfDocument) throws IOException;

    void uploadOldBook(PDFDocument pdfDocument);

    void deletePaper(@PathVariable Long uploadedPaperId);
}
