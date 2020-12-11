package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.client.PaymentGatewayClient;
import nitpicksy.bank.constants.BankConstants;
import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.enumeration.TransactionStatus;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Merchant;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.repository.PaymentRequestRepository;
import nitpicksy.bank.service.CreditCardService;
import nitpicksy.bank.service.MerchantService;
import nitpicksy.bank.service.PaymentService;
import nitpicksy.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class PaymentServiceImpl implements PaymentService {

    private PaymentRequestRepository paymentRequestRepository;

    private CreditCardService creditCardService;

    private TransactionService transactionService;

    private PaymentGatewayClient paymentGatewayClient;

    @Override
    public PaymentResponseDTO pay(PaymentRequest paymentRequest) {
        if(paymentRequestRepository.findByMerchantOrderId(paymentRequest.getMerchantOrderId()) != null){
            throw new InvalidDataException("Payment Request for this order already exist.", HttpStatus.BAD_REQUEST);
        }
        PaymentRequest createdPaymentRequest = paymentRequestRepository.save(paymentRequest);
        return new PaymentResponseDTO(createdPaymentRequest.getId(), BankConstants.PAYMENT_URL + createdPaymentRequest.getId());
    }

    //transferInsideBank
    //transferBetweenBanks
    @Override
    public String confirmPayment(ConfirmPaymentDTO confirmPaymentDTO, Long paymentId) throws NoSuchAlgorithmException {
        PaymentRequest paymentRequest = paymentRequestRepository.findOneById(paymentId);
        if(paymentRequest == null){
            throw new InvalidDataException("Payment request doesn't exist.", HttpStatus.BAD_REQUEST);
        }
        if(transactionService.findByMerchantOrderId(paymentRequest.getMerchantOrderId()) != null){
            throw new InvalidDataException("Transaction for this payment request already exist.", HttpStatus.BAD_REQUEST);
        }

        if(creditCardService.isClientOfThisBank(confirmPaymentDTO.getPAN())){
            CreditCard creditCard = creditCardService.checkCreditCardDate(confirmPaymentDTO.getPAN(),confirmPaymentDTO.getCardHolderName(),
                    confirmPaymentDTO.getExpirationDate(),confirmPaymentDTO.getSecurityCode());
            Transaction transaction = transactionService.transferInsideBank(paymentRequest, creditCard);

            sendResponseToPaymentGateway(transaction.getMerchantOrderId(),transaction.getStatus(), paymentId);
            return getRedirectURL(transaction.getStatus(),paymentRequest);
        }else {

        }

        return paymentRequest.getErrorUrl();
    }

    @Async
    public void sendResponseToPaymentGateway(Long  merchantOrderId, TransactionStatus status,Long paymentId){
        ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentId,status.toString());
        paymentGatewayClient.confirmPayment(merchantOrderId,confirmPaymentResponseDTO);
    }

    private String getRedirectURL(TransactionStatus status, PaymentRequest paymentRequest){
        if(status.equals(TransactionStatus.SUCCESS)){
            return paymentRequest.getSuccessUrl();
        }

        if(status.equals(TransactionStatus.FAILED)){
            return paymentRequest.getFailedUrl();
        }
        return paymentRequest.getErrorUrl();
    }
    @Autowired
    public PaymentServiceImpl(PaymentRequestRepository paymentRequestRepository,CreditCardService creditCardService,
                              TransactionService transactionService,PaymentGatewayClient paymentGatewayClient) {
        this.paymentRequestRepository = paymentRequestRepository;
        this.creditCardService = creditCardService;
        this.transactionService = transactionService;
        this.paymentGatewayClient = paymentGatewayClient;
    }
}
