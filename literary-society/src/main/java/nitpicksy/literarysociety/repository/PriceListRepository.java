package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    PriceList findTopByStartDateLessThanEqualOrderByStartDateDesc(LocalDate startDate);
}
