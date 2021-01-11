package nitpicksy.bank.controller;

import nitpicksy.bank.serviceImpl.TestServiceImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;

@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestController {

    private TestServiceImpl testService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("Greetings from bank");
        return new ResponseEntity<>(testService.healthCheck(), HttpStatus.OK);
    }

    @GetMapping("/encrypted")
    public ResponseEntity<String> getEncryptedValue(@RequestParam String value) {
        byte[] secret = loadSecret().getBytes();
        Key key = new SecretKeySpec(secret, "AES");
        try {
            Cipher cipher= Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new ResponseEntity<>(Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes())), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("BAD", HttpStatus.BAD_REQUEST);
    }

    private String loadSecret(){
        try {
            Path fileStorageLocation = Paths.get("");
            Path filePath = fileStorageLocation.resolve("bank_key.np").normalize();
            Resource resource = new UrlResource(filePath.toUri());
            File file = resource.getFile();
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Autowired
    public TestController(TestServiceImpl testService) {
        this.testService = testService;
    }
}
