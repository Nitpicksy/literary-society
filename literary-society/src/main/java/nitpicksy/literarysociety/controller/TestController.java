package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.serviceimpl.TestServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/ls-test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    private TestServiceImpl testService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>(testService.healthCheck(), HttpStatus.OK);
    }

    @Autowired
    public TestController(TestServiceImpl testService) {
        this.testService = testService;
    }
}
