package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.request.CommitteeOpinionDTO;
import nitpicksy.literarysociety.dto.response.CommentsDTO;

import java.util.List;

public interface CommitteeOpinionService {

    void save(CommitteeOpinionDTO dto);

    List<CommentsDTO> getWriterComments();

}
