package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Reader findByUsername(String username);

    List<Reader> findByIsBetaReaderAndBetaReaderGenresIdAndStatus(boolean isBetaReader, Long id, UserStatus status);

    List<Reader> findByIdIn(List<Long> ids);
}
