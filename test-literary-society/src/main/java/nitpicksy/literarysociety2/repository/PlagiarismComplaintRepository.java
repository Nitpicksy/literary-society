package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.PlagiarismComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlagiarismComplaintRepository extends JpaRepository<PlagiarismComplaint, Long> {
}
