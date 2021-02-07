package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.camunda.service.CamundaService;
import nitpicksy.literarysociety2.client.ZuulClient;
import nitpicksy.literarysociety2.constants.CamundaConstants;
import nitpicksy.literarysociety2.dto.request.ChangePasswordDTO;
import nitpicksy.literarysociety2.dto.request.JWTRequestDTO;
import nitpicksy.literarysociety2.dto.request.RequestTokenDTO;
import nitpicksy.literarysociety2.dto.request.ResetPasswordDTO;
import nitpicksy.literarysociety2.exceptionHandler.BlockedUserException;
import nitpicksy.literarysociety2.exceptionHandler.InvalidTokenException;
import nitpicksy.literarysociety2.exceptionHandler.InvalidUserDataException;
import nitpicksy.literarysociety2.model.JWTToken;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.UserTokenState;
import nitpicksy.literarysociety2.repository.JWTTokenRepository;
import nitpicksy.literarysociety2.security.JwtAuthenticationRequest;
import nitpicksy.literarysociety2.service.AuthenticationService;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.UserService;
import nitpicksy.literarysociety2.serviceimpl.TestServiceImpl;
import nitpicksy.literarysociety2.utils.IPAddressProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private AuthenticationService authenticationService;

    private LogService logService;

    private IPAddressProvider ipAddressProvider;

    private TestServiceImpl testService;

    private CamundaService camundaService;

    private JWTTokenRepository jwtTokenRepository;

    private ZuulClient zuulClient;

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

    @PutMapping(value = "/activate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> activateAccount(@RequestParam(required = false) String piId, @RequestParam String t) {
        try {
            authenticationService.activateAccount(t);
            if (piId != null && !piId.isEmpty() && !piId.equals("null")) {
                camundaService.findAndCompleteActiveTask(piId);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidTokenException("Activation token cannot be checked. Please try again.", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> generateResetToken(@Valid @RequestBody RequestTokenDTO requestTokenDTO) {
        try {
            userService.generateResetToken(requestTokenDTO.getUsername());
        } catch (NoSuchAlgorithmException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RST", "Hash algorithm threw exception"));
            throw new InvalidTokenException("Reset token cannot be generated. Please try again.",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> resetPassword(
            @RequestParam @Pattern(regexp = "^([0-9a-fA-F]{8})-(([0-9a-fA-F]{4}-){3})([0-9a-fA-F]{12})$", message = "This reset token is invalid.") String t,
            @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getRepeatedPassword())) {
                throw new InvalidUserDataException("Passwords do not match", HttpStatus.BAD_REQUEST);
            }
            authenticationService.resetPassword(t, resetPasswordDTO);
        } catch (NullPointerException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RPW", String.format("Invalid username or password provided from %s", ipAddressProvider.get())));
            throw new InvalidUserDataException("Invalid username or password.", HttpStatus.BAD_REQUEST);
        } catch (NoSuchAlgorithmException e) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "RPW", "External password check failed"));
            throw new InvalidUserDataException("Reset token or new password cannot be checked. Please try again.",
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(null);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(userService.refreshAuthenticationToken(request));
    }

    @PostMapping(value = "/accept-pg-token")
    public ResponseEntity<Void> acceptPaymentGatewayToken(@NotBlank @RequestBody JWTRequestDTO jwtRequestDTO) {
        jwtTokenRepository.save(new JWTToken(jwtRequestDTO.getJwtToken(), jwtRequestDTO.getRefreshJwt()));
        executeRefreshToken(jwtRequestDTO.getRefreshJwt(), jwtRequestDTO.getExpirationDate());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Async
    public void executeRefreshToken(String refreshJWTToken, Date expirationDate) {
        Instant instantExpirationDate = (expirationDate).toInstant();
        Instant executionMoment = instantExpirationDate.plus(1, ChronoUnit.MINUTES);
        ScheduledExecutorService localExecutor = Executors.newSingleThreadScheduledExecutor();
        TaskScheduler scheduler = new ConcurrentTaskScheduler(localExecutor);
        scheduler.schedule(createRunnable(refreshJWTToken), executionMoment);
    }

    private Runnable createRunnable(final String refreshJWTToken) {
        return () -> {
            JWTRequestDTO jwtRequestDTO = zuulClient.refreshAuthenticationToken("Bearer " + refreshJWTToken);
            changeJWTToken(jwtRequestDTO);
            executeRefreshToken(jwtRequestDTO.getRefreshJwt(), jwtRequestDTO.getExpirationDate());
        };
    }

    private void changeJWTToken(JWTRequestDTO jwtRequestDTO) {
        JWTToken jwtToken = jwtTokenRepository.findById(1L).get();
        jwtToken.setToken(jwtRequestDTO.getJwtToken());
        jwtToken.setRefreshToken(jwtRequestDTO.getRefreshJwt());
        jwtTokenRepository.save(jwtToken);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        System.out.println("Greetings!");
        camundaService.messageEventReceived(CamundaConstants.MESSAGE_PAYMENT_SUCCESS, "rascal");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/health2")
    public ResponseEntity<String> health2() {
        System.out.println("Greetings!");
        camundaService.messageEventReceived(CamundaConstants.MESSAGE_PAYMENT_ERROR, "rascal");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public AuthenticationController(UserService userService, AuthenticationService authenticationService, LogService logService, IPAddressProvider ipAddressProvider, TestServiceImpl testService, CamundaService camundaService, JWTTokenRepository jwtTokenRepository, ZuulClient zuulClient) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.logService = logService;
        this.ipAddressProvider = ipAddressProvider;
        this.testService = testService;
        this.camundaService = camundaService;
        this.jwtTokenRepository = jwtTokenRepository;
        this.zuulClient = zuulClient;
    }
}
