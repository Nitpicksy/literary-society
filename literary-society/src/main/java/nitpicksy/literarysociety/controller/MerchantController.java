package nitpicksy.literarysociety.controller;
import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.MerchantRequestDTO;
import nitpicksy.literarysociety.dto.request.UserRequestDTO;
import nitpicksy.literarysociety.dto.response.MerchantResponseDTO;
import nitpicksy.literarysociety.dto.response.UserResponseDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.mapper.MerchantRequestMapper;
import nitpicksy.literarysociety.mapper.MerchantResponseMapper;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.User;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/merchants", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {

    private MerchantService merchantService;

    private UserService userService;

    private Environment environment;

    private MerchantRequestMapper merchantRequestMapper;

    private MerchantResponseMapper merchantResponseMapper;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MerchantResponseDTO> signUp(@Valid @RequestBody MerchantRequestDTO merchantRequestDTO) throws NoSuchAlgorithmException {
        if(!merchantRequestDTO.getPassword().equals(merchantRequestDTO.getRepeatedPassword())){
            throw new InvalidDataException("Password and repeated password don't match",HttpStatus.BAD_REQUEST);
        }

        Merchant merchant = merchantRequestMapper.toEntity(merchantRequestDTO);
        merchant.setRole(userService.findRoleByName(RoleConstants.ROLE_MERCHANT));

        if (userService.findByUsername(merchant.getUsername()) != null) {
            throw new InvalidDataException("User with same username already exist",HttpStatus.BAD_REQUEST);
        }

        if (userService.findByEmail(merchant.getEmail()) != null) {
            throw new InvalidDataException("User with same email already exist",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(merchantResponseMapper.toDto(merchantService.signUp(merchant)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MerchantResponseDTO>> findByStatus() {

        return new ResponseEntity<>(merchantService.findByStatusIn(Arrays.asList(UserStatus.WAITING_APPROVAL,
                UserStatus.ACTIVE)).stream()
                .map(user -> merchantResponseMapper.toDto(user)).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MerchantResponseDTO> changeUserStatus(@PathVariable @Positive Long id,
                                                            @RequestParam @Pattern(regexp = "(?i)(approve|reject)$", message = "Status is not valid.") String status) {
        return new ResponseEntity<>(merchantResponseMapper.toDto(merchantService.changeUserStatus(id, status)), HttpStatus.OK);
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");
    }

    @Autowired

    public MerchantController(MerchantService merchantService, UserService userService, Environment environment,
                              MerchantRequestMapper merchantRequestMapper, MerchantResponseMapper merchantResponseMapper) {
        this.merchantService = merchantService;
        this.userService = userService;
        this.environment = environment;
        this.merchantRequestMapper = merchantRequestMapper;
        this.merchantResponseMapper = merchantResponseMapper;
    }
}
