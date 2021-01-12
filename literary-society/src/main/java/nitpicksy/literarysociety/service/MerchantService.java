package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

public interface MerchantService {

    Merchant findByName(String name);

    String getPaymentData(Merchant merchant);

    Merchant save(Merchant merchant);

    Merchant findOurMerchant();

    Merchant signUp(Merchant merchant) throws NoSuchAlgorithmException;

    List<Merchant> findByStatusIn(Collection<UserStatus> status);

    Merchant changeUserStatus(Long id, String status);

    void synchronizeMerchants();

    void changeSupportPaymentMethods(Boolean supportPaymentMethods);
}
