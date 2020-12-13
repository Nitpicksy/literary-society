package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.client.ZuulClient;
import nitpicksy.bank.constants.BankConstants;
import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.request.PayRequestDTO;
import nitpicksy.bank.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.bank.dto.response.PaymentResponseDTO;
import nitpicksy.bank.enumeration.TransactionStatus;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.repository.PaymentRequestRepository;
import nitpicksy.bank.service.AccountService;
import nitpicksy.bank.service.CreditCardService;
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

    private ZuulClient zuulClient;

    private AccountService accountService;

    @Override
    public PaymentResponseDTO pay(PaymentRequest paymentRequest) {
        if(paymentRequestRepository.findByMerchantOrderId(paymentRequest.getMerchantOrderId()) != null){
            throw new InvalidDataException("Payment Request for this order already exist.", HttpStatus.BAD_REQUEST);
        }
        PaymentRequest createdPaymentRequest = paymentRequestRepository.save(paymentRequest);
        return new PaymentResponseDTO(createdPaymentRequest.getId(), BankConstants.PAYMENT_URL + createdPaymentRequest.getId());
    }

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
            ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentId,transaction.getStatus().toString());
            sendResponseToPaymentGateway(confirmPaymentResponseDTO,transaction.getMerchantOrderId());
            return getRedirectURL(transaction.getStatus(),paymentRequest);
        }

        ConfirmPaymentResponseDTO confirmPaymentResponseDTO = transactionService.transferBetweenBanks(paymentRequest,confirmPaymentDTO);
        sendResponseToPaymentGateway(confirmPaymentResponseDTO,paymentRequest.getMerchantOrderId());
        return getRedirectURL(TransactionStatus.valueOf(confirmPaymentResponseDTO.getStatus()),paymentRequest);
    }

    @Async
    public void sendResponseToPaymentGateway(ConfirmPaymentResponseDTO confirmPaymentResponseDTO,Long merchantOrderId){
        zuulClient.confirmPayment(merchantOrderId,confirmPaymentResponseDTO);
    }

    @Override
    public Transaction payClientBank(PayRequestDTO payRequestDTO) {
        CreditCard creditCard = creditCardService.checkCreditCardDateHashedValues(payRequestDTO.getConfirmPaymentDTO().getPAN(),
                payRequestDTO.getConfirmPaymentDTO().getCardHolderName(),payRequestDTO.getConfirmPaymentDTO().getExpirationDate(),payRequestDTO.getConfirmPaymentDTO().getSecurityCode());

        TransactionStatus status = TransactionStatus.SUCCESS;
        if(creditCard == null){
            status = TransactionStatus.FAILED;
            return transactionService.create(payRequestDTO.getAmount(), payRequestDTO.getMerchantId(),
                    payRequestDTO.getMerchantOrderId(), payRequestDTO.getMerchantTimestamp(),payRequestDTO.getPaymentId(),payRequestDTO.getConfirmPaymentDTO().getPAN(), status);
        }

        if(!accountService.hasEnoughMoney(payRequestDTO.getAmount(),creditCard )){
            status = TransactionStatus.FAILED;
        }

        return transactionService.create(payRequestDTO.getAmount(), payRequestDTO.getMerchantId(),
                payRequestDTO.getMerchantOrderId(), payRequestDTO.getMerchantTimestamp(),payRequestDTO.getPaymentId(),payRequestDTO.getConfirmPaymentDTO().getPAN(), status);
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
    public PaymentServiceImpl(PaymentRequestRepository paymentRequestRepository, CreditCardService creditCardService,
                              TransactionService transactionService, ZuulClient zuulClient,AccountService accountService) {
        this.paymentRequestRepository = paymentRequestRepository;
        this.creditCardService = creditCardService;
        this.transactionService = transactionService;
        this.zuulClient = zuulClient;
        this.accountService = accountService;
    }
}
