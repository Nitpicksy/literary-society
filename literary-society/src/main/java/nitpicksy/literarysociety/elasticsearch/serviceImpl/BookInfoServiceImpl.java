package nitpicksy.literarysociety.elasticsearch.serviceImpl;

import nitpicksy.literarysociety.elasticsearch.model.BookInfo;
import nitpicksy.literarysociety.elasticsearch.repository.BookInfoRepository;
import nitpicksy.literarysociety.elasticsearch.service.BookInfoService;
import nitpicksy.literarysociety.elasticsearch.service.GenreInfoService;
import nitpicksy.literarysociety.elasticsearch.utils.ApacheTikaHandler;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.plagiarist.utils.UploadFile;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    private BookInfoRepository bookInfoRepository;

    private UploadFile uploadFile;

    private GenreInfoService genreInfoService;

    private ApacheTikaHandler apacheTikaHandler;

    @Async("processExecutor")
    @Override
    public BookInfo index(Book book) {
        BookInfo bookInfo = new BookInfo(book.getId(), book.getTitle(), book.getWritersNames(),
                book.getPublishingInfo().getPrice() != null && book.getPublishingInfo().getPrice() <= 0,
                genreInfoService.findById(book.getGenre().getId()));

        try {
            bookInfo.setContent(getContent(book.getId()));
            return bookInfoRepository.save(bookInfo);
        } catch (IOException e) {
            return null;
        }
    }

    private String getContent(Long bookId) throws IOException {
        PDFDocument pdfDocument = uploadFile.findByBookId(bookId);
        File book = uploadFile.download(pdfDocument);
        return apacheTikaHandler.extractContentUsingParser(new FileInputStream(book));
    }

    public BookInfoServiceImpl(BookInfoRepository bookInfoRepository,UploadFile uploadFile,
                               GenreInfoService genreInfoService, ApacheTikaHandler apacheTikaHandler) {
        this.bookInfoRepository = bookInfoRepository;
        this.uploadFile = uploadFile;
        this.genreInfoService = genreInfoService;
        this.apacheTikaHandler = apacheTikaHandler;

    }
}
