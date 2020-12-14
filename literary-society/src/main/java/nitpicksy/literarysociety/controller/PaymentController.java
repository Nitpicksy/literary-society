package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.dto.response.BookDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.mapper.BookDtoMapper;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.PaymentService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private PaymentService paymentService;

    private BookDtoMapper bookDtoMapper;

    private UserService userService;

    private BookService bookService;

    @PostMapping(value= "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> proceedToPayment(@Valid @RequestBody List<BookDTO> bookDTOS) {
        List<Long> booksIds = bookDTOS.stream().map(BookDTO::getId).collect(Collectors.toList());
        if(booksIds.size() != bookDTOS.size()){
            throw new InvalidUserDataException("Please choose existing books.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(paymentService.proceedToPayment(bookService.findByIds(booksIds),userService.getAuthenticatedUser()),HttpStatus.OK);
    }

    @Autowired
    public PaymentController(PaymentService paymentService,BookDtoMapper bookDtoMapper,UserService userService, BookService bookService) {
        this.paymentService = paymentService;
        this.bookDtoMapper = bookDtoMapper;
        this.userService = userService;
        this.bookService = bookService;
    }
}
