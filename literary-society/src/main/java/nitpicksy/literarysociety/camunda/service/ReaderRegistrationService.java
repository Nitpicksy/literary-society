package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.constants.RoleConstants;
import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.ReaderRepository;
import nitpicksy.literarysociety.service.GenreService;
import nitpicksy.literarysociety.service.VerificationService;
import nitpicksy.literarysociety.serviceimpl.UserServiceImpl;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReaderRegistrationService implements JavaDelegate {

    private UserServiceImpl userService;

    private PasswordEncoder passwordEncoder;

    private ReaderRepository readerRepository;

    private VerificationService verificationService;

    private GenreService genreService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        boolean isBetaReader = Boolean.parseBoolean(map.get("isBetaReader"));

        Reader reader = new Reader(map.get("firstName"), map.get("lastName"), map.get("city"), map.get("country"), map.get("email"),
                map.get("username"), map.get("password"), isBetaReader);

        if (isBetaReader) {
            List<Long> ids = new ArrayList<>();
            String[] genresStr = map.get("selectGenres").split(",");
            for (String idStr : genresStr) {
                if (idStr.contains("_")) {
                    Long id = Long.valueOf(idStr.split("_")[1]);
                    ids.add(id);
                }
            }
            List<Genre> genres = genreService.findWithIds(ids);
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

        return savedReader;
    }


    @Autowired
    public ReaderRegistrationService(UserServiceImpl userService, PasswordEncoder passwordEncoder, ReaderRepository readerRepository,
                                     VerificationService verificationService, Environment environment, GenreService genreService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.readerRepository = readerRepository;
        this.verificationService = verificationService;
        this.genreService = genreService;
    }
}
