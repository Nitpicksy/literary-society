package nitpicksy.bank.service;

import nitpicksy.bank.model.Merchant;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface MerchantService {

    Merchant findByMerchantIdAndPassword(String merchantId, String merchantPassword) throws NoSuchAlgorithmException;

    void transferMoneyToMerchant(String merchantId, Double amount) throws NoSuchAlgorithmException;

    List<Merchant> findAll();

    Merchant save(Merchant merchant) throws NoSuchAlgorithmException;
}
