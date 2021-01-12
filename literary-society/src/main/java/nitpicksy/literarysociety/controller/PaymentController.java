package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.dto.request.LiterarySocietyOrderRequestDTO;
import nitpicksy.literarysociety.dto.response.BookDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety.mapper.BookDtoMapper;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.service.BookService;
import nitpicksy.literarysociety.service.MerchantService;
import nitpicksy.literarysociety.service.PaymentService;
import nitpicksy.literarysociety.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private PaymentService paymentService;

    private UserService userService;

    private BookService bookService;

    private Environment environment;

    private MerchantService merchantService;

    @PostMapping(value = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> proceedToBookPayment(@Valid @RequestBody List<BookDTO> bookDTOS) {
        List<Long> booksIds = bookDTOS.stream().map(BookDTO::getId).collect(Collectors.toList());
        if (booksIds.size() != bookDTOS.size()) {
            throw new InvalidUserDataException("Please choose existing books.", HttpStatus.BAD_REQUEST);
        }
        String redirectUrl = paymentService.proceedToBookPayment(bookService.findByIds(booksIds), userService.getAuthenticatedUser());
        System.out.println("redirectUrl: " + redirectUrl);
        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }

    @PostMapping(value = "/pay-membership", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> proceedToMembershipPayment() {
        String redirectUrl = paymentService.proceedToMembershipPayment(userService.getAuthenticatedUser());
        System.out.println("redirectUrl: " + redirectUrl);
        return new ResponseEntity<>(redirectUrl, HttpStatus.OK);
    }


    @PostMapping(value = "/confirm", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmPayment(@Valid @RequestBody LiterarySocietyOrderRequestDTO requestDTO) throws NoSuchAlgorithmException {
        paymentService.handlePayment(requestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/choose-payment-methods")
    public ResponseEntity<String> choosePaymentMethods() {
        return new ResponseEntity<>(paymentService.choosePaymentMethods(), HttpStatus.OK);
    }

    @PutMapping("/choose-payment-methods")
    public ResponseEntity<String> choosePaymentMethods(@RequestBody @NotNull Boolean supportPaymentMethods) {
        if(!supportPaymentMethods){
            changeSupportPaymentMethods(supportPaymentMethods);
        }
        return new ResponseEntity<>(getLocalhostURL(), HttpStatus.OK);
    }

    @Async
    public void changeSupportPaymentMethods(Boolean supportPaymentMethods){
        merchantService.changeSupportPaymentMethods(supportPaymentMethods);
    }
    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public PaymentController(PaymentService paymentService, UserService userService, BookService bookService,Environment environment,MerchantService merchantService) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.bookService = bookService;
        this.merchantService = merchantService;
        this.environment = environment;
    }
}
