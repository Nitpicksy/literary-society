package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.OpinionOfEditorAboutComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionOfEditorAboutComplaintRepository extends JpaRepository<OpinionOfEditorAboutComplaint, Long> {

    List<OpinionOfEditorAboutComplaint> findByPlagiarismComplaintId(Long id);
}
