package nitpicksy.pcc.repository;

import nitpicksy.pcc.model.INCodeBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface INCodeBookRepository extends JpaRepository<INCodeBook,Long> {

    INCodeBook findByIin(String iin);
}
