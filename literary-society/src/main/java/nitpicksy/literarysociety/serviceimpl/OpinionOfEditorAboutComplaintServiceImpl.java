package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.dto.camunda.EditorsCommentsDTO;
import nitpicksy.literarysociety.model.OpinionOfEditorAboutComplaint;
import nitpicksy.literarysociety.repository.OpinionOfEditorAboutComplaintRepository;
import nitpicksy.literarysociety.service.OpinionOfEditorAboutComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpinionOfEditorAboutComplaintServiceImpl implements OpinionOfEditorAboutComplaintService {

    private OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository;

    @Override
    public List<EditorsCommentsDTO> findOpinionsByPlagiarismComplaint(Long id) {
        List<OpinionOfEditorAboutComplaint> opinions = opinionOfEditorAboutComplaintRepository.findByPlagiarismComplaintId(id);

        List<EditorsCommentsDTO> list = new ArrayList<>();
        for (OpinionOfEditorAboutComplaint opinion : opinions) {
            list.add(new EditorsCommentsDTO(
                    opinion.getId(),
                    opinion.getReview(),
                    opinion.getEditor().getUsername(),
                    opinion.getEditor().getFirstName() + ' ' + opinion.getEditor().getLastName()
            ));
        }

        return list;
    }

    @Autowired
    public OpinionOfEditorAboutComplaintServiceImpl(OpinionOfEditorAboutComplaintRepository opinionOfEditorAboutComplaintRepository) {
        this.opinionOfEditorAboutComplaintRepository = opinionOfEditorAboutComplaintRepository;
    }
}
