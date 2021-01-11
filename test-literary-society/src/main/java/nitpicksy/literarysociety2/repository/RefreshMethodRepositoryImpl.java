package nitpicksy.literarysociety2.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class RefreshMethodRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements RefreshMethodRepository<T, ID> {

    private final EntityManager entityManager;

    public RefreshMethodRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void refresh(T t) {
        entityManager.refresh(t);
    }
}
