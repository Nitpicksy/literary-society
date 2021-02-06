package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.OpinionOfEditor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpinionOfEditorRepository extends JpaRepository<OpinionOfEditor, Long> {

    List<OpinionOfEditor> findByBookIdOrderByCreatedDesc(Long bookId);

}
