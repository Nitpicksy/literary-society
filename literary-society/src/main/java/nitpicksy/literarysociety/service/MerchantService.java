package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Merchant;

public interface MerchantService {

    Merchant findByName(String name);

    String getPaymentData(Merchant merchant);

    Merchant save(Merchant merchant);

    Merchant findOurMerchant();
}
