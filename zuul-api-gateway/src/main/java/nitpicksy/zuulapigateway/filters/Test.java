package nitpicksy.zuulapigateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private URLConfig urlConfig;

    @GetMapping("/hello")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>("Hi! " + urlConfig.getTestValue(), HttpStatus.OK);
    }

}
