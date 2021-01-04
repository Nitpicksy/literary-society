package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatusAndPublishingInfoMerchantSupportsPaymentMethods(BookStatus status, boolean supportsPaymentMethods);

    Set<Book> findByIdIn(Collection<Long> id);

    Book findOneById(Long id);

    List<Book> findByWriterId(Long writerId);

}
