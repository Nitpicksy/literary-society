package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.client.ZuulClient;
import nitpicksy.literarysociety.exceptionHandler.FileNotFoundException;
import nitpicksy.literarysociety.exceptionHandler.InvalidDataException;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.repository.MerchantRepository;
import nitpicksy.literarysociety.service.JWTTokenService;
import nitpicksy.literarysociety.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService  {

    private MerchantRepository merchantRepository;

    private ZuulClient zuulClient;

    private JWTTokenService jwtTokenService;

    @Override
    public Merchant findByName(String name) {
        return merchantRepository.findByName(name);
    }

    @Override
    public String getPaymentData(Merchant merchant) {
        ResponseEntity<String> response = zuulClient.getPaymentData("Bearer " + jwtTokenService.getToken(), merchant.getName());
        if(response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }
        throw new InvalidDataException("Something went wrong.Please try again.", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository, ZuulClient zuulClient,JWTTokenService jwtTokenService) {
        this.merchantRepository = merchantRepository;
        this.zuulClient = zuulClient;
        this.jwtTokenService=jwtTokenService;
    }
}
