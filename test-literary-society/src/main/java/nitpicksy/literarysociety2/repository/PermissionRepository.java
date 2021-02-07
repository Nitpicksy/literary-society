package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);
}

