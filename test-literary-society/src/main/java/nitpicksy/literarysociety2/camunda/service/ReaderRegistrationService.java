package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.constants.RoleConstants;
import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety2.model.Genre;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.Reader;
import nitpicksy.literarysociety2.repository.ReaderRepository;
import nitpicksy.literarysociety2.service.GenreService;
import nitpicksy.literarysociety2.service.LogService;
import nitpicksy.literarysociety2.service.UserService;
import nitpicksy.literarysociety2.service.VerificationService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReaderRegistrationService implements JavaDelegate {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    private ReaderRepository readerRepository;

    private VerificationService verificationService;

    private GenreService genreService;

    private CamundaService camundaService;

    private LogService logService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        boolean isBetaReader = Boolean.parseBoolean(map.get("isBetaReader"));

        Reader reader = new Reader(map.get("firstName"), map.get("lastName"), map.get("city"), map.get("country"), map.get("email"),
                map.get("username"), map.get("password"), isBetaReader);

        if (isBetaReader) {
            List<Long> genresIds = camundaService.extractIds(map.get("selectGenres"));
            List<Genre> genres = genreService.findWithIds(genresIds);
            if (genres.isEmpty()) {
                throw new InvalidDataException("You have to choose at least one genre.", HttpStatus.BAD_REQUEST);
            }
            reader.setGenres(new HashSet<>(genres));
        }

        create(reader, execution);
    }

    public Reader create(Reader reader, DelegateExecution execution) throws NoSuchAlgorithmException {
        if (userService.findByUsername(reader.getUsername()) != null) {
            throw new BpmnError("greskaKreiranjeCitaoca");
        }

        if (userService.findByEmail(reader.getEmail()) != null) {
            throw new BpmnError("greskaKreiranjeCitaoca");
        }

        reader.setStatus(UserStatus.NON_VERIFIED);
        reader.setRole(userService.findRoleByName(RoleConstants.ROLE_READER));
        String password = reader.getPassword();
        reader.setPassword(passwordEncoder.encode(password));
        Reader savedReader = readerRepository.save(reader);
        String nonHashedToken = verificationService.generateToken(savedReader);

        execution.setVariable("token", nonHashedToken);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDR",
                String.format("Reader %s successfully created",savedReader.getId())));

        return savedReader;
    }

    @Autowired
    public ReaderRegistrationService(UserService userService, PasswordEncoder passwordEncoder,
                                     ReaderRepository readerRepository, VerificationService verificationService,
                                     GenreService genreService, CamundaService camundaService,LogService logService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.readerRepository = readerRepository;
        this.verificationService = verificationService;
        this.genreService = genreService;
        this.camundaService = camundaService;
        this.logService = logService;
    }
}
