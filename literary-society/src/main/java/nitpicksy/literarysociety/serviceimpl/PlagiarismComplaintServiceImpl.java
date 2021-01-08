package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import nitpicksy.literarysociety.service.PlagiarismComplaintService;
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
