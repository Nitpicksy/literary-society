package nitpicksy.literarysociety.controller;

import nitpicksy.literarysociety.dto.response.PriceListDTO;
import nitpicksy.literarysociety.mapper.PriceListDtoMapper;
import nitpicksy.literarysociety.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/price-lists")
public class PriceListController {

    private PriceListDtoMapper priceListDtoMapper;
    private PriceListService priceListService;

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceListDTO> getLatest() {
        return new ResponseEntity<>(priceListDtoMapper.toDto(priceListService.findLatestPriceList()), HttpStatus.OK);
    }

    @Autowired
    public PriceListController(PriceListDtoMapper priceListDtoMapper, PriceListService priceListService) {
        this.priceListDtoMapper = priceListDtoMapper;
        this.priceListService = priceListService;
    }
}
