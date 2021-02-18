package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.client.PlagiatorClient;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.elastic.service.BookIndexService;
import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.PublishingInfo;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.ImageRepository;
import nitpicksy.literarysociety.repository.MerchantRepository;
import nitpicksy.literarysociety.repository.PublishingInfoRepository;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublishingInfoService implements JavaDelegate {

    private BookRepository bookRepository;

    private PublishingInfoRepository publishingInfoRepository;

    private MerchantRepository merchantRepository;

    private BookIndexService bookIndexService;

    private PDFDocumentService pdfDocumentService;

    private PlagiatorClient plagiatorClient;

    private Environment environment;

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
        PublishingInfo savedPublishingInfo = publishingInfoRepository.save(publishingInfo);
        book.setPublishingInfo(savedPublishingInfo);

        // Indexing new book
        bookIndexService.addBook(book);
        // Deleting old and uploading new version in Plagiator
        Long uploadedPaperId = Long.valueOf((String) execution.getVariable(("uploadedPaperId")));
        plagiatorClient.deletePaper(uploadedPaperId);
        MultipartFile multipartFile = pdfDocumentService.convertToMultipartFile(book.getId());
        plagiatorClient.uploadExistingBook(multipartFile);

        System.out.println("*** Kraj procesa izdavanja knjige ***");
    }

    private String getPublisher() {
        return environment.getProperty("PUBLISHER");
    }

    @Autowired
    public PublishingInfoService(BookRepository bookRepository, PublishingInfoRepository publishingInfoRepository,
                                 MerchantRepository merchantRepository, BookIndexService bookIndexService,
                                 PlagiatorClient plagiatorClient, PDFDocumentService pdfDocumentService, Environment environment) {
        this.bookRepository = bookRepository;
        this.publishingInfoRepository = publishingInfoRepository;
        this.merchantRepository = merchantRepository;
        this.bookIndexService = bookIndexService;
        this.pdfDocumentService = pdfDocumentService;
        this.plagiatorClient = plagiatorClient;
        this.environment = environment;
    }
}
