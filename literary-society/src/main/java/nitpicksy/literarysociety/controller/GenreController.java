package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/genres", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenreController {

    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getAll() {
        return new ResponseEntity<>(genreService.findAll(), HttpStatus.OK);
    }

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }
}
