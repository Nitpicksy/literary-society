package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findOneById(Long id);

    List<User> findByRoleNameAndStatus(String roleName, UserStatus status);

    List<User> findByRoleNameAndStatusInOrRoleNameAndStatusIn(String roleName1, Collection<UserStatus> status1, String roleName2, Collection<UserStatus> status2);

    List<User> findByIdIn(List<Long> ids);
}
