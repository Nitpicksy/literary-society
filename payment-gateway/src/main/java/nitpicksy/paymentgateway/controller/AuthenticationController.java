package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.request.ChangePasswordDTO;
import nitpicksy.paymentgateway.dto.request.JWTRequestDTO;
import nitpicksy.paymentgateway.dto.response.UserTokenState;
import nitpicksy.paymentgateway.exceptionHandler.BlockedUserException;
import nitpicksy.paymentgateway.exceptionHandler.InvalidUserDataException;
import nitpicksy.paymentgateway.model.Log;
import nitpicksy.paymentgateway.security.JwtAuthenticationRequest;
import nitpicksy.paymentgateway.service.AuthenticationService;
import nitpicksy.paymentgateway.service.LogService;
import nitpicksy.paymentgateway.service.UserService;
import nitpicksy.paymentgateway.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private AuthenticationService authenticationService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    @PostMapping(value = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTokenState> login(@Valid @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try {
            UserTokenState userTokenState = authenticationService.login(authenticationRequest);
            if (userTokenState == null) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "LGN", String.format("Invalid username or password provided from %s", ipAddressProvider.get())));
                throw new UsernameNotFoundException("Invalid username or password. Please try again.");
            }
            return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(userTokenState);
        } catch (AuthenticationException e) {
            if (authenticationService.userIsNeverLoggedIn(authenticationRequest.getUsername())) {
                return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
            }
            if (e.getMessage().equals("Blocked")) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "LGN", String.format("User from %s is blocked.", ipAddressProvider.get())));
                throw new BlockedUserException(
                        "You tried to log in too many times. Your account wil be blocked for the next 24 hours.",
                        HttpStatus.BAD_REQUEST);
            }
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "LGN", String.format("Invalid username or password provided from %s", ipAddressProvider.get())));
            throw new UsernameNotFoundException("Invalid username or password. Please try again.");
        } catch (NullPointerException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "LGN", String.format("Invalid username or password provided from %s", ipAddressProvider.get())));
            throw new UsernameNotFoundException("Invalid username or password. Please try again.");
        }
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getRepeatedPassword())) {
                throw new InvalidUserDataException("Passwords do not match", HttpStatus.BAD_REQUEST);
            }
            authenticationService.changePassword(changePasswordDTO);
        } catch (NullPointerException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", String.format("Invalid email or password provided from %s", ipAddressProvider.get())));
            throw new InvalidUserDataException("Invalid email  or password.", HttpStatus.BAD_REQUEST);
        } catch (NoSuchAlgorithmException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPW", "External password check failed"));
            throw new InvalidUserDataException("Password cannot be checked. Please try again.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(null);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(userService.refreshAuthenticationToken(request));
    }

    @RequestMapping(value = "/company-refresh", method = RequestMethod.POST)
    public ResponseEntity<JWTRequestDTO> companyRefreshAuthenticationToken(HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(userService.companyRefreshAuthenticationToken(request));
    }


    @Autowired
    public AuthenticationController(UserService userService, AuthenticationService authenticationService, LogService logService,
                                    IPAddressProvider ipAddressProvider) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
    }
}