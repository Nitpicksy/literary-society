package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.OpinionOfEditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpinionOfEditorRepository extends JpaRepository<OpinionOfEditor,Long> {
}
