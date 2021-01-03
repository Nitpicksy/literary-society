package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findOneById(Long id);

    List<User> findByRoleName(String roleName);

    List<User> findByIdIn(List<Long> ids);

    List<User> findByRoleNameAndStatusOrRoleNameAndStatus(String roleName1, UserStatus status1, String roleName2, UserStatus status2);
}
