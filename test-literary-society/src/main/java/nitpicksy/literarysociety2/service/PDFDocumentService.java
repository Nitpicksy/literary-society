package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.camunda.WriterDocumentDTO;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.PDFDocument;
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
}
