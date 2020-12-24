package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.PDFDocument;

import java.io.IOException;
import java.net.URISyntaxException;

public interface PDFDocumentService {

    byte[] download(String name) throws IOException, URISyntaxException;

    PDFDocument findByBookId(Long id);
}
