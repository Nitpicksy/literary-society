package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    Genre findOneById(Long id);

    List<Genre> findByIdIn(List<Long> ids);

}
