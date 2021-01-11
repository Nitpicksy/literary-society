package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.PlagiarismComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlagiarismComplaintRepository extends JpaRepository<PlagiarismComplaint, Long> {
}
