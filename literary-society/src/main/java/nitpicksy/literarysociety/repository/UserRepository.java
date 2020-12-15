package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findOneById(Long id);

    void deleteByUsername(String username);

}
