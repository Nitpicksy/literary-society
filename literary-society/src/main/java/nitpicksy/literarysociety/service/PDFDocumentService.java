package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.camunda.WriterDocumentDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipFile;

public interface PDFDocumentService {

    PDFDocument upload(MultipartFile pdfFile, Book book) throws IOException;

    byte[] download(String name) throws IOException, URISyntaxException;

    PDFDocument findByBookId(Long id);

    List<PDFDocument> findByBooks(Set<Book> books);

    List<WriterDocumentDTO> getDraftsByWriter(String writer);

    File download(PDFDocument pdfDocument) throws IOException;

    String extractText(PDFDocument pdfDocument) throws IOException;

}
