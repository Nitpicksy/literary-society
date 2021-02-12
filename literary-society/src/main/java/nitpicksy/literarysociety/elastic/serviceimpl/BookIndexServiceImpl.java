package nitpicksy.literarysociety.elastic.serviceimpl;

import nitpicksy.literarysociety.elastic.model.BookIndexingUnit;
import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.elastic.repository.BookIndexRepository;
import nitpicksy.literarysociety.elastic.service.BookIndexService;
import nitpicksy.literarysociety.elastic.service.GenreIndexService;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.service.PDFDocumentService;
import nitpicksy.literarysociety.serviceimpl.PDFDocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class BookIndexServiceImpl implements BookIndexService {

    private ElasticsearchTemplate elasticsearchTemplate;

    private BookIndexRepository bookIndexRepository;

    private GenreIndexService genreIndexService;

    private PDFDocumentService pdfDocumentService;

    @Async("processExecutor")
    @Override
    public BookIndexingUnit addBook(Book book) {
        BookIndexingUnit bookIdxUnit = new BookIndexingUnit();
        bookIdxUnit.setId(book.getId());
        bookIdxUnit.setTitle(book.getTitle());
        bookIdxUnit.setWriters(book.getWritersNames());

        GenreIndexingUnit genreIdxUnit = genreIndexService.findById(book.getGenre().getId());
        bookIdxUnit.setGenre(genreIdxUnit);

        boolean openAccess = book.getPublishingInfo().getPrice() <= 0;
        bookIdxUnit.setOpenAccess(openAccess);

        String text;
        PDFDocument bookPDFDoc = pdfDocumentService.findByBookId(book.getId());
        try {
            text = pdfDocumentService.extractText(bookPDFDoc);
        } catch (IOException e) {
            text = "";
        }
        bookIdxUnit.setText(text);

        return bookIndexRepository.save(bookIdxUnit);
    }

    @Autowired
    public BookIndexServiceImpl(ElasticsearchTemplate elasticsearchTemplate, BookIndexRepository bookIndexRepository,
                                GenreIndexService genreIndexService, PDFDocumentService pdfDocumentService) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.bookIndexRepository = bookIndexRepository;
        this.genreIndexService = genreIndexService;
        this.pdfDocumentService = pdfDocumentService;
    }

}
