package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByName(String name);
}

