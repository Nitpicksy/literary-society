package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    PriceList findTopByStartDateLessThanEqualOrderByStartDateDesc(LocalDate startDate);
}
