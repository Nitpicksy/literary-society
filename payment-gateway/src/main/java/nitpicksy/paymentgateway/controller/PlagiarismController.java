package nitpicksy.paymentgateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(value = "/api/plagiarism", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlagiarismController {

    @PostMapping("/check-book")
    public ResponseEntity<Integer> checkBook(@RequestBody byte[] pdfBytes) {
        Integer percentage = new Random().nextInt(100);
        return new ResponseEntity<>(percentage, HttpStatus.OK);
    }
}
