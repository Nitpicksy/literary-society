package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.enumeration.BookStatus;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.PublishingInfo;
import nitpicksy.literarysociety2.repository.BookRepository;
import nitpicksy.literarysociety2.repository.ImageRepository;
import nitpicksy.literarysociety2.repository.MerchantRepository;
import nitpicksy.literarysociety2.repository.PublishingInfoRepository;
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
        publishingInfoRepository.save(publishingInfo);

        System.out.println("*** Kraj procesa izdavanja knjige ***");
    }

    private String getPublisher() {
        return environment.getProperty("PUBLISHER");
    }

    @Autowired
    public PublishingInfoService(BookRepository bookRepository, ImageRepository imageRepository, PublishingInfoRepository publishingInfoRepository, MerchantRepository merchantRepository, Environment environment) {
        this.bookRepository = bookRepository;
        this.imageRepository = imageRepository;
        this.publishingInfoRepository = publishingInfoRepository;
        this.merchantRepository = merchantRepository;
        this.environment = environment;
    }
}
