package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.OpinionOfBetaReader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpinionOfBetaReaderRepository extends JpaRepository<OpinionOfBetaReader, Long> {

    List<OpinionOfBetaReader> findByBookId(Long bookId);

}
