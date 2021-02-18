package nitpicksy.literarysociety.camunda.service;

import com.google.gson.Gson;
import nitpicksy.literarysociety.client.PlagiatorClient;
import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.elastic.dto.PaperResultDTO;
import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.service.JWTTokenService;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CheckIfBookIsPlagiarism implements JavaDelegate {

    private PDFDocumentService pdfDocumentService;

    private PlagiatorClient plagiatorClient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));

        MultipartFile multipartFile = pdfDocumentService.convertToMultipartFile(bookId);
        PaperResultDTO paperResultDTO = plagiatorClient.uploadNewBook(multipartFile);

        String jsonSimilarPapers = new Gson().toJson(paperResultDTO.getSimilarPapers());
        execution.setVariable("similarPapers", jsonSimilarPapers);
        execution.setVariable("paperResultId", paperResultDTO.getId().toString());
        execution.setVariable("uploadedPaperId", paperResultDTO.getUploadedPaper().getId().toString());
    }

    @Autowired
    public CheckIfBookIsPlagiarism(PDFDocumentService pdfDocumentService, PlagiatorClient plagiatorClient) {
        this.pdfDocumentService = pdfDocumentService;
        this.plagiatorClient = plagiatorClient;
    }
}
