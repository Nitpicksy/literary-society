package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.dto.request.CommitteeOpinionDTO;
import nitpicksy.literarysociety.service.CommitteeOpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/committee")
public class CommitteeController {

    private CommitteeOpinionService committeeOpinionService;

    @PostMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> vote(@Valid @RequestBody CommitteeOpinionDTO opinionDTO) {
        committeeOpinionService.save(opinionDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public CommitteeController(CommitteeOpinionService committeeOpinionService) {
        this.committeeOpinionService = committeeOpinionService;
    }
}
