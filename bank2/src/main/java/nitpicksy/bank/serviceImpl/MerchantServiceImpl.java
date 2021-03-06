package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.repository.MerchantRepository;
import nitpicksy.bank.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class MerchantServiceImpl implements MerchantService {

    private MerchantRepository merchantRepository;

    @Override
    public Merchant findByMerchantIdAndPassword(String merchantId, String merchantPassword) {
        Merchant merchant = merchantRepository.findByMerchantId(merchantId);

        if(merchant == null){
            throw new InvalidDataException("Invalid merchant id or password. Please try again.", HttpStatus.BAD_REQUEST);
        }

        if(!merchant.getMerchantPassword().equals(merchantPassword)){
            throw new InvalidDataException("Invalid merchant id or password. Please try again.", HttpStatus.BAD_REQUEST);
        }

        return merchant;
    }

    @Override
    public void transferMoneyToMerchant(String merchantId, Double amount) throws NoSuchAlgorithmException {
        Merchant merchant = merchantRepository.findByMerchantId(merchantId);
        merchant.setBalance(merchant.getBalance() + amount);
        merchantRepository.save(merchant);
    }

    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }
}
