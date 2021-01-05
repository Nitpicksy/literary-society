package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Merchant;

public interface MerchantService {

    Merchant findByNameAndCompany(String name, Long companyId);

    Merchant findByIdAndCompany(Long merchantId, Long companyId);

    Merchant save(Merchant merchant);
}
