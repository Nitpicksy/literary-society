package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Reader findByUsername(String username);

    List<Reader> findByIsBetaReaderAndBetaReaderGenresIdAndStatus(boolean isBetaReader, Long id, UserStatus status);

    List<Reader> findByIsBetaReaderAndStatus(boolean isBetaReader, UserStatus status);

    List<Reader> findByIdIn(List<Long> ids);
}
