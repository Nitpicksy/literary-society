package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Long> {

    Writer findByUsername(String username);

    Writer findByEmail(String email);

    Writer findOneById(Long id);

}
