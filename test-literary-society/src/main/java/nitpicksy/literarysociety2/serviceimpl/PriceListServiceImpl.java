package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.model.PriceList;
import nitpicksy.literarysociety2.repository.PriceListRepository;
import nitpicksy.literarysociety2.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PriceListServiceImpl implements PriceListService {

    private PriceListRepository priceListRepository;

    @Override
    public PriceList findLatestPriceList() {
        return priceListRepository.findTopByStartDateLessThanEqualOrderByStartDateDesc(LocalDate.now());
    }

    @Autowired
    public PriceListServiceImpl(PriceListRepository priceListRepository) {
        this.priceListRepository = priceListRepository;
    }
}
