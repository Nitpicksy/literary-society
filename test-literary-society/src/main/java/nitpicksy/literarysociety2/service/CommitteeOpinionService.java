package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.request.CommitteeOpinionDTO;
import nitpicksy.literarysociety2.dto.response.CommentsDTO;

import java.util.List;

public interface CommitteeOpinionService {

    void save(CommitteeOpinionDTO dto);

    List<CommentsDTO> getWriterComments();

}
