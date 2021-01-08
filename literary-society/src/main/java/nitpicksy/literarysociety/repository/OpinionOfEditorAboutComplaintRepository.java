package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.OpinionOfEditorAboutComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionOfEditorAboutComplaintRepository extends JpaRepository<OpinionOfEditorAboutComplaint, Long> {
}
