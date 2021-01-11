package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpinionOfBetaReaderRepository extends JpaRepository<OpinionOfBetaReader, Long> {

    List<OpinionOfBetaReader> findByBookId(Long bookId);

}
