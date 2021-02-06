package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.dto.request.CommitteeOpinionDTO;
import nitpicksy.literarysociety2.dto.response.CommentsDTO;
import nitpicksy.literarysociety2.service.CommitteeOpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/committee")
public class CommitteeController {

    private CommitteeOpinionService committeeOpinionService;


    @PostMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> vote(@Valid @RequestBody CommitteeOpinionDTO opinionDTO) {
        committeeOpinionService.save(opinionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentsDTO>> getWriterComments() {

        return new ResponseEntity<>(committeeOpinionService.getWriterComments(), HttpStatus.OK);
    }

    @Autowired
    public CommitteeController(CommitteeOpinionService committeeOpinionService) {
        this.committeeOpinionService = committeeOpinionService;
    }
}
