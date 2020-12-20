package nitpicksy.bank.service;

import nitpicksy.bank.model.Merchant;

import java.security.NoSuchAlgorithmException;

public interface MerchantService {

    Merchant findByMerchantIdAndPassword(String merchantId, String merchantPassword) throws NoSuchAlgorithmException;

    void transferMoneyToMerchant(String merchantId, Double amount) throws NoSuchAlgorithmException;
}
