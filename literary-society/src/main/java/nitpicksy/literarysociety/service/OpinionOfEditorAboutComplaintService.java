package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.camunda.EditorsCommentsDTO;

import java.util.List;

public interface OpinionOfEditorAboutComplaintService {

    List<EditorsCommentsDTO> findOpinionsByPlagiarismComplaint(Long id);
}
