package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.constants.BankConstants;
import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.repository.PaymentRequestRepository;
import nitpicksy.bank.service.CreditCardService;
import nitpicksy.bank.service.MerchantService;
import nitpicksy.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRequestRepository paymentRequestRepository;

    private CreditCardService creditCardService;

    @Override
    public PaymentResponseDTO pay(PaymentRequest paymentRequest) {
        PaymentRequest createdPaymentRequest = paymentRequestRepository.save(paymentRequest);
        return new PaymentResponseDTO(createdPaymentRequest.getId(), BankConstants.PAYMENT_URL + paymentRequest.getId());
    }

    @Override
    public String confirmPayment(ConfirmPaymentDTO confirmPaymentDTO) throws NoSuchAlgorithmException {
        CreditCard creditCard = creditCardService.checkCreditCardDate(confirmPaymentDTO.getPAN(),confirmPaymentDTO.getCardHolderName(),
                confirmPaymentDTO.getExpirationDate(),confirmPaymentDTO.getSecurityCode());

        return "haj";
    }

    @Autowired
    public PaymentServiceImpl(PaymentRequestRepository paymentRequestRepository,CreditCardService creditCardService) {
        this.paymentRequestRepository = paymentRequestRepository;
        this.creditCardService = creditCardService;
    }
}
