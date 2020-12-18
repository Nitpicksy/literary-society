package nitpicksy.bitcoinservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("Greetings from bitcoin");
        return new ResponseEntity<>("Bitcoin is up and running!", HttpStatus.OK);
    }

}
