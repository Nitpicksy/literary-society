package nitpicksy.literarysociety.camunda.service;

import com.google.gson.Gson;
import nitpicksy.literarysociety.client.PlagiaristClient;
import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PDFDocument;
import nitpicksy.literarysociety.plagiarist.dto.PaperResultDTO;
import nitpicksy.literarysociety.service.JWTTokenService;
import nitpicksy.literarysociety.service.PDFDocumentService;
import org.apache.commons.io.IOUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class CheckIfBookIsPlagiarism implements JavaDelegate {

    private PDFDocumentService pdfDocumentService;

    private PlagiaristClient plagiaristClient;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));

        PDFDocument pdfDocument = pdfDocumentService.findByBookId(bookId);
        try{

            FileInputStream input = new FileInputStream(pdfDocumentService.download(pdfDocument));
            MultipartFile multipartFile = new MockMultipartFile(pdfDocument.getName(),pdfDocument.getName(),
                    "text/plain", IOUtils.toByteArray(input));

            PaperResultDTO resultDTO =  plagiaristClient.uploadNew(multipartFile);

            String serialized = new Gson().toJson(resultDTO.getSimilarPapers());

            execution.setVariable("similarPapers", serialized);
        } catch (RuntimeException | IOException exception) {
            System.out.println("Error");
        }

    }

    @Autowired
    public CheckIfBookIsPlagiarism(PDFDocumentService pdfDocumentService, PlagiaristClient plagiaristClient) {
        this.pdfDocumentService = pdfDocumentService;
        this.plagiaristClient = plagiaristClient;
    }
}
