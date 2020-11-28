package nitpicksy.zuulapigateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value="api/")
@RefreshScope
public class Test {

    @Value("${test.value}")
    private String testValue;

    @Value("${bitcoin.uri}")
    private String bitcoinUri;

    @GetMapping("/hello")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>("Hi! " + testValue + "[Bitcoin URI]: " + bitcoinUri, HttpStatus.OK);
    }

}
