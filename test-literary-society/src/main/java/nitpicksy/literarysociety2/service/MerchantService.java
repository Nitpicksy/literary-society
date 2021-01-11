package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.model.Merchant;

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
}
