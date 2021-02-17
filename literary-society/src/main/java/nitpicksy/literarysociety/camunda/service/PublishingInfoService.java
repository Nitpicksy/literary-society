package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.client.PlagiaristClient;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.elasticsearch.service.BookInfoService;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.model.PublishingInfo;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.ImageRepository;
import nitpicksy.literarysociety.repository.MerchantRepository;
import nitpicksy.literarysociety.repository.PublishingInfoRepository;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublishingInfoService implements JavaDelegate {

    private BookRepository bookRepository;

    private ImageRepository imageRepository;

    private PublishingInfoRepository publishingInfoRepository;

    private MerchantRepository merchantRepository;

    private Environment environment;

    private BookInfoService bookInfoService;

    private PDFDocumentService pdfDocumentService;

    @Override
    public void execute(DelegateExecution execution) {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));

        Book book = bookRepository.findOneById(bookId);
        book.setStatus(BookStatus.IN_STORES);
        Book savedBook = bookRepository.save(book);

        Integer numberOfPages = Integer.valueOf(map.get("numberOfPages"));
        Double price = Double.valueOf(map.get("price"));
        Integer discount = Integer.valueOf(map.get("discount"));
        PublishingInfo publishingInfo = new PublishingInfo(numberOfPages, map.get("publisherCity"), getPublisher(), price, discount, savedBook, merchantRepository.findFirstByOrderByIdAsc());
        PublishingInfo savedPublishingInfo = publishingInfoRepository.saveAndFlush(publishingInfo);
        book.setPublishingInfo(savedPublishingInfo);

        bookInfoService.index(book);

        Long uploadedPaperId = Long.valueOf((String) execution.getVariable("uploadedPaperId"));
        System.out.println(uploadedPaperId);

        pdfDocumentService.deletePaper(uploadedPaperId);
        pdfDocumentService.uploadOldBook(pdfDocumentService.findByBookId(savedBook.getId()));

        System.out.println("*** Kraj procesa izdavanja knjige ***");
    }

    private String getPublisher() {
        return environment.getProperty("PUBLISHER");
    }

    @Autowired
    public PublishingInfoService(BookRepository bookRepository, ImageRepository imageRepository,
                                 PublishingInfoRepository publishingInfoRepository, MerchantRepository merchantRepository,
                                 Environment environment,  BookInfoService bookInfoService,PDFDocumentService pdfDocumentService) {
        this.bookRepository = bookRepository;
        this.imageRepository = imageRepository;
        this.publishingInfoRepository = publishingInfoRepository;
        this.merchantRepository = merchantRepository;
        this.environment = environment;
        this.bookInfoService = bookInfoService;
        this.pdfDocumentService = pdfDocumentService;
    }
}
