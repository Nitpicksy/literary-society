package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.dto.request.PaymentGatewayPayRequestDTO;
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
import org.springframework.stereotype.Service;

@Service
public class CheckIfBookIsPlagiarism implements JavaDelegate {

    private PDFDocumentService pdfDocumentService;

    private ZuulClient zuulClient;

    private JWTTokenService jwtTokenService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));

        PDFDocument pdfDocument = pdfDocumentService.findByBookId(bookId);
        try{
            byte[] pdfBytes = pdfDocumentService.download(pdfDocument.getName());

            Integer percentage = zuulClient.checkBook("Bearer " + jwtTokenService.getToken(), pdfBytes);

            execution.setVariable("percentage", Integer.toString(percentage));
        } catch (RuntimeException exception) {
            throw new BpmnError("Something went wrong. Please try again.");
        }

    }

    @Autowired
    public CheckIfBookIsPlagiarism(PDFDocumentService pdfDocumentService, ZuulClient zuulClient, JWTTokenService jwtTokenService) {
        this.pdfDocumentService = pdfDocumentService;
        this.zuulClient = zuulClient;
        this.jwtTokenService = jwtTokenService;
    }
}
