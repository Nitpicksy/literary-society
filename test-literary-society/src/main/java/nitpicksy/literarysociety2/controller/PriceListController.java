package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.dto.response.PriceListDTO;
import nitpicksy.literarysociety2.mapper.PriceListDtoMapper;
import nitpicksy.literarysociety2.service.PriceListService;
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
