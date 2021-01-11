package nitpicksy.literarysociety2.camunda.handler;

import nitpicksy.literarysociety2.dto.camunda.EnumKeyValueDTO;
import nitpicksy.literarysociety2.model.Genre;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.service.GenreService;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WriterRegistrationProcessHandler implements ExecutionListener {

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
        execution.setVariable("selectGenresList", enumList);

        User user = userService.getAuthenticatedUser();
        
        if (user instanceof Writer) {
            execution.setVariable("writer", user.getUsername());
        }
    }

    @Autowired
    public WriterRegistrationProcessHandler(GenreService genreService, UserService userService) {
        this.genreService = genreService;
        this.userService = userService;
    }
}
