package nitpicksy.literarysociety.camunda.handler;

import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.service.GenreService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReaderRegistrationProcessHandler implements ExecutionListener {

    private GenreService genreService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        List<Genre> genresList = genreService.findAll();
        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (Genre genre : genresList) {
            String key = "id_" + genre.getId();
            enumList.add(new EnumKeyValueDTO(key, genre.getName()));
        }
        execution.setVariable("selectGenresList", enumList);
        execution.setVariable("selectBetaReaderGenresList", enumList);
    }

    @Autowired
    public ReaderRegistrationProcessHandler(GenreService genreService) {
        this.genreService = genreService;
    }
}
