package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.enumeration.BookStatus;
import nitpicksy.literarysociety2.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatusAndPublishingInfoMerchantSupportsPaymentMethods(BookStatus status, boolean supportsPaymentMethods);

    List<Book> findByStatusAndPublishingInfoMerchantSupportsPaymentMethodsAndPublishingInfoMerchantId(BookStatus status, boolean supportsPaymentMethods, Long merchantId);

    Set<Book> findByIdIn(Collection<Long> id);

    Book findOneById(Long id);

    List<Book> findByWriterId(Long writerId);
    
    Book findFirstByTitleContainingAndWritersNamesContaining(String title, String writer);

}
