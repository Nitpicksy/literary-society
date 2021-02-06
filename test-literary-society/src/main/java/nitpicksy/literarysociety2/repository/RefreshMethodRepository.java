package nitpicksy.literarysociety2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface RefreshMethodRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    void refresh(T t);
}
