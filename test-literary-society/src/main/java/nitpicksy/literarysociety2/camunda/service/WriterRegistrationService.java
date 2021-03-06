package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.constants.RoleConstants;
import nitpicksy.literarysociety2.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety2.model.Genre;
import nitpicksy.literarysociety2.model.Log;
import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.repository.WriterRepository;
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
public class WriterRegistrationService implements JavaDelegate {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private UserService userService;

    private GenreService genreService;

    private CamundaService camundaService;

    private PasswordEncoder passwordEncoder;

    private WriterRepository writerRepository;

    private VerificationService verificationService;

    private LogService logService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        Writer writer = new Writer(map.get("firstName"), map.get("lastName"), map.get("city"), map.get("country"), map.get("email"),
                map.get("username"), map.get("password"));

        List<Long> genresIds = camundaService.extractIds(map.get("selectGenres"));
        List<Genre> genres = genreService.findWithIds(genresIds);

        if (genres.isEmpty()) {
            throw new InvalidDataException("You have to choose at least one genre.", HttpStatus.BAD_REQUEST);
        }
        writer.setGenre(new HashSet<>(genres));
        create(writer, execution);
    }


    public Writer create(Writer writer, DelegateExecution execution) throws NoSuchAlgorithmException {

        if (userService.findByUsername(writer.getUsername()) != null) {
            throw new BpmnError("greskaKreiranjePisca");
        }

        if (userService.findByEmail(writer.getEmail()) != null) {
            throw new BpmnError("greskaKreiranjePisca");
        }

        writer.setStatus(UserStatus.NON_VERIFIED);
        writer.setRole(userService.findRoleByName(RoleConstants.ROLE_WRITER));
        String password = writer.getPassword();
        writer.setPassword(passwordEncoder.encode(password));
        Writer savedWriter = writerRepository.save(writer);
        String nonHashedToken = verificationService.generateToken(savedWriter);

        execution.setVariable("token", nonHashedToken);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "ADDW",
                String.format("Writer %s  successfully created",savedWriter.getId())));
        return savedWriter;
    }

    @Autowired
    public WriterRegistrationService(UserService userService, GenreService genreService, CamundaService camundaService,
                                     PasswordEncoder passwordEncoder, WriterRepository writerRepository, VerificationService verificationService,
                                     LogService logService) {
        this.userService = userService;
        this.genreService = genreService;
        this.camundaService = camundaService;
        this.passwordEncoder = passwordEncoder;
        this.writerRepository = writerRepository;
        this.verificationService = verificationService;
        this.logService = logService;
    }

}
