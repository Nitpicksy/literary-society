package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.ReaderRepository;
import nitpicksy.literarysociety.service.GenreService;
import nitpicksy.literarysociety.service.ReaderService;
import nitpicksy.literarysociety.service.VerificationService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReaderServiceImpl implements ReaderService, JavaDelegate {

    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;

    private ReaderRepository readerRepository;

    private VerificationService verificationService;

    private GenreService genreService;

    private Environment environment;

    @Override
    public Reader create(Reader reader, DelegateExecution execution) throws NoSuchAlgorithmException {
        if (userService.findByUsername(reader.getUsername()) != null) {
            execution.setVariable("success", false);
            throw new InvalidDataException("User with same username already exists.", HttpStatus.BAD_REQUEST);
        }

        if (userService.findByEmail(reader.getEmail()) != null) {
            execution.setVariable("success", false);
            throw new InvalidDataException("User with same email already exists.", HttpStatus.BAD_REQUEST);
        }

        reader.setStatus(UserStatus.NON_VERIFIED);
        reader.setRole(userService.findRoleByName(RoleConstants.ROLE_READER));
        String password = reader.getPassword();
        reader.setPassword(passwordEncoder.encode(password));

        Reader savedReader = readerRepository.save(reader);
        String nonHashedToken = verificationService.generateToken(savedReader);

        execution.setVariable("success", true);
        execution.setVariable("email", reader.getEmail());
        execution.setVariable("subject", "Account activation");
        execution.setVariable("text", composeEmailToActivate(nonHashedToken));
        return savedReader;
    }

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        boolean isBetaReader = Boolean.parseBoolean(map.get("isBetaReader"));

        Reader reader = new Reader(map.get("firstName"), map.get("lastName"), map.get("city"), map.get("country"), map.get("email"),
                map.get("username"), map.get("password"), isBetaReader);

        if (isBetaReader) {
            List<Long> ids = new ArrayList<>();
            String[] genresStr = map.get("genres").split(",");
            for (String idStr : genresStr) {
                Long id = Long.valueOf(idStr.split("_")[1]);
                ids.add(id);
            }
            List<Genre> genres = genreService.findWithIds(ids);
            reader.setGenres(new HashSet<>(genres));
        }

        create(reader,execution);
    }

    private String composeEmailToActivate(String token) {
        StringBuilder sb = new StringBuilder();
        sb.append("You have successfully registered to the Literary Society website.");
        sb.append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("To activate your account click the following link:");
        sb.append(System.lineSeparator());
        sb.append(getLocalhostURL());
        sb.append("activate-account=" + token);
        String text = sb.toString();
        return text;
    }

    private String getLocalhostURL() {
        return environment.getProperty("LOCALHOST_URL");

    }


    @Autowired
    public ReaderServiceImpl(UserServiceImpl userService, PasswordEncoder passwordEncoder, ReaderRepository readerRepository,
                             VerificationService verificationService, Environment environment,GenreService genreService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.readerRepository = readerRepository;
        this.verificationService = verificationService;
        this.environment = environment;
        this.genreService = genreService;
    }
}
