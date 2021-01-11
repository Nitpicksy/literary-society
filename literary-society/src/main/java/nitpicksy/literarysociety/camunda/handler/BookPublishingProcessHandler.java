package nitpicksy.literarysociety.camunda.handler;

import nitpicksy.literarysociety.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.model.Writer;
import nitpicksy.literarysociety.service.GenreService;
import nitpicksy.literarysociety.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookPublishingProcessHandler implements ExecutionListener {

    private GenreService genreService;

    private UserService userService;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        List<Genre> genresList = genreService.findAll();
        List<EnumKeyValueDTO> enumList = new ArrayList<>();
        for (Genre genre : genresList) {
            String key = "id_" + genre.getId();
            enumList.add(new EnumKeyValueDTO(key, genre.getName()));
        }
        execution.setVariable("selectGenreList", enumList);

        Writer writer = (Writer) userService.getAuthenticatedUser();
        execution.setVariable("writer", writer.getUsername());
    }

    @Autowired
    public BookPublishingProcessHandler(GenreService genreService, UserService userService) {
        this.genreService = genreService;
        this.userService = userService;
    }
}
