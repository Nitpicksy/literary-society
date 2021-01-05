package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.model.Merchant;
import nitpicksy.paymentgateway.repository.MerchantRepository;
import nitpicksy.paymentgateway.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService {

    private MerchantRepository merchantRepository;

    @Override
    public Merchant findByNameAndCompany(String name, Long companyId) {
        return merchantRepository.findByNameAndCompanyId(name,companyId);
    }

    @Override
    public Merchant findByIdAndCompany(Long merchantId, Long companyId) {
        return merchantRepository.findByIdAndCompanyId(merchantId,companyId);
    }

    @Override
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }
}
