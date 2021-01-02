package nitpicksy.literarysociety.controller;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.service.MerchantService;
import nitpicksy.literarysociety.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/merchants", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {

    private MerchantService merchantService;

    private UserService userService;

    private Environment environment;

    @GetMapping("/payment-data")
    public ResponseEntity<String> getPaymentData() {
        Merchant merchant = userService.getAuthenticatedMerchant();
        if(merchant.isSupportsPaymentMethods()){
            return new ResponseEntity<>("", HttpStatus.OK);
        }
        return new ResponseEntity<>(merchantService.getPaymentData(merchant), HttpStatus.OK);
    }

    @PostMapping("/{name}/payment-data")
    public ResponseEntity<String> supportPaymentMethods(@PathVariable String name) {
        Merchant merchant = merchantService.findByName(name);
        if(merchant == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        merchant.setSupportsPaymentMethods(true);
        merchantService.save(merchant);

        return new ResponseEntity<>(getLocalhostURL(), HttpStatus.OK);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired
    public MerchantController(MerchantService merchantService, UserService userService,Environment environment) {
        this.merchantService = merchantService;
        this.userService = userService;
        this.environment = environment;
    }
}
