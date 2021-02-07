package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.model.PlagiarismComplaint;
import nitpicksy.literarysociety2.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety2.service.PlagiarismComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlagiarismComplaintServiceImpl implements PlagiarismComplaintService {

    private PlagiarismComplaintRepository plagiarismComplaintRepository;

    @Override
    public PlagiarismComplaint findById(Long id) {
        return plagiarismComplaintRepository.findById(id).orElse(null);
    }

    @Autowired
    public PlagiarismComplaintServiceImpl(PlagiarismComplaintRepository plagiarismComplaintRepository) {
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
    }
}
