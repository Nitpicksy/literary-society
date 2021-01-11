package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.camunda.EditorsCommentsDTO;

import java.util.List;

public interface OpinionOfEditorAboutComplaintService {

    List<EditorsCommentsDTO> findOpinionsByPlagiarismComplaint(Long id);
}
