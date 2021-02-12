package nitpicksy.literarysociety.camunda.service;

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
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublishingInfoService implements JavaDelegate {

    private BookRepository bookRepository;

    private ImageRepository imageRepository;

    private PublishingInfoRepository publishingInfoRepository;

    private MerchantRepository merchantRepository;

    private BookIndexService bookIndexService;

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

        System.out.println("*** Kraj procesa izdavanja knjige ***");
    }

    private String getPublisher() {
        return environment.getProperty("PUBLISHER");
    }

    @Autowired
    public PublishingInfoService(BookRepository bookRepository, ImageRepository imageRepository, PublishingInfoRepository publishingInfoRepository,
                                 MerchantRepository merchantRepository, BookIndexService bookIndexService, Environment environment) {
        this.bookRepository = bookRepository;
        this.imageRepository = imageRepository;
        this.publishingInfoRepository = publishingInfoRepository;
        this.merchantRepository = merchantRepository;
        this.bookIndexService = bookIndexService;
        this.environment = environment;
    }
}
