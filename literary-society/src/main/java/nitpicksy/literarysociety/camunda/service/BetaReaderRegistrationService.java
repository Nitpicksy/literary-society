package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.ReaderRepository;
import nitpicksy.literarysociety.service.GenreService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BetaReaderRegistrationService implements JavaDelegate {

    private GenreService genreService;

    private ReaderRepository readerRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Reader reader = readerRepository.findByUsername((String) execution.getVariable("username"));
        if (reader == null) {
            throw new InvalidDataException("You are not a Beta-reader, therefore you cannot choose these genres.", HttpStatus.BAD_REQUEST);
        }

        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));
        List<Long> ids = new ArrayList<>();
        String[] genresStr = map.get("selectBetaReaderGenres").split(",");
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
        reader.setBetaReaderGenres(new HashSet<>(genres));
        readerRepository.save(reader);
    }

    @Autowired
    public BetaReaderRegistrationService(GenreService genreService, ReaderRepository readerRepository) {
        this.genreService = genreService;
        this.readerRepository = readerRepository;
    }

}
