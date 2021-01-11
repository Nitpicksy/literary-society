package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Merchant;

import java.util.List;

public interface MerchantService {

    Merchant findByNameAndCompany(String name, Long companyId);

    Merchant findByIdAndCompany(Long merchantId, Long companyId);

    Merchant save(Merchant merchant);

    List<Merchant> findByCompany(Long companyId);
}
