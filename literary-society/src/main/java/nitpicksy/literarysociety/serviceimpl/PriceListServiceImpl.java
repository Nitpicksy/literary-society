package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.PriceList;
import nitpicksy.literarysociety.repository.PriceListRepository;
import nitpicksy.literarysociety.service.PriceListService;
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
