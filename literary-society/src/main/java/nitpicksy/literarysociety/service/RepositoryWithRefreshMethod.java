package nitpicksy.literarysociety.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface RepositoryWithRefreshMethod<T, ID extends Serializable> extends JpaRepository<T, ID> {
    void refresh(T t);
}
