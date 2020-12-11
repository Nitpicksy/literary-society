package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByStatusAndPublishingInfoMerchantSupportsPaymentMethods(BookStatus status, boolean supportsPaymentMethods);

}
