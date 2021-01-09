package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.response.LiterarySocietyOrderResponseDTO;
import nitpicksy.paymentgateway.mapper.LiterarySocietyOrderMapper;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.service.LogService;
import nitpicksy.paymentgateway.service.TransactionService;
import nitpicksy.paymentgateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private TransactionService transactionService;

    private LogService logService;

    private LiterarySocietyOrderMapper literarySocietyOrderMapper;

    @GetMapping
    public ResponseEntity<List<LiterarySocietyOrderResponseDTO>> findAll() {
        Company company = userService.getAuthenticatedCompany();
        if (company == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "GETT", "Company not found"));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(transactionService.findAll(company.getId()).stream()
                .map(literarySocietyOrderMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @Autowired
    public TransactionController(UserService userService, TransactionService transactionService,LogService logService,
                                 LiterarySocietyOrderMapper literarySocietyOrderMapper) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.logService = logService;
        this.literarySocietyOrderMapper = literarySocietyOrderMapper;
    }
}
