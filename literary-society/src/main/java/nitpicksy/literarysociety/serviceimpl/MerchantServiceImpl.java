package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.repository.MerchantRepository;
import nitpicksy.literarysociety.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService  {

    private MerchantRepository merchantRepository;

    @Override
    public Merchant findByName(String name) {
        return merchantRepository.findByName(name);
    }

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }
}
