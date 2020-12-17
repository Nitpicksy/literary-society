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
import nitpicksy.bank.model.Log;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.repository.PaymentRequestRepository;
import nitpicksy.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private PaymentRequestRepository paymentRequestRepository;

    private CreditCardService creditCardService;

    private TransactionService transactionService;

    private ZuulClient zuulClient;

    private AccountService accountService;

    private LogService logService;

    @Override
    public PaymentResponseDTO pay(PaymentRequest paymentRequest) {
        if (paymentRequestRepository.findByMerchantOrderId(paymentRequest.getMerchantOrderId()) != null) {

            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PAY", String.format("Payment Request for this order already exist.")));
            throw new InvalidDataException("Payment Request for this order already exist.", HttpStatus.BAD_REQUEST);
        }
        PaymentRequest createdPaymentRequest = paymentRequestRepository.save(paymentRequest);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "PAY", String.format("Payment request %s is successfully created.", createdPaymentRequest.getId())));
        return new PaymentResponseDTO(createdPaymentRequest.getId(), BankConstants.PAYMENT_URL + createdPaymentRequest.getId());
    }

    @Override
    public String confirmPayment(ConfirmPaymentDTO confirmPaymentDTO, Long paymentId) throws NoSuchAlgorithmException {
        // kad budemo imali 3 LU-a, merchantOrderId se moze poklapati
        PaymentRequest paymentRequest = paymentRequestRepository.findOneById(paymentId);
        if (paymentRequest == null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Payment request %s doesn't exist.", paymentId)));
            throw new InvalidDataException("Payment request doesn't exist.", HttpStatus.BAD_REQUEST);
        }
        
        if (transactionService.findByMerchantOrderId(paymentRequest.getMerchantOrderId()) != null) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction for payment request %s already exist", paymentId)));
            throw new InvalidDataException("Transaction for this payment request already exist.", HttpStatus.BAD_REQUEST);
        }

        if (creditCardService.isClientOfThisBank(confirmPaymentDTO.getPAN())) {
            CreditCard creditCard = creditCardService.checkCreditCardDate(confirmPaymentDTO.getPAN(), confirmPaymentDTO.getCardHolderName(),
                    confirmPaymentDTO.getExpirationDate(), confirmPaymentDTO.getSecurityCode());
            Transaction transaction = transactionService.transferInsideBank(paymentRequest, creditCard);
            ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentId, transaction.getStatus().toString());
            sendResponseToPaymentGateway(confirmPaymentResponseDTO, transaction.getMerchantOrderId());
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", transaction.getId(), transaction.getStatus())));
            return getRedirectURL(transaction.getStatus(), paymentRequest);
        }

        ConfirmPaymentResponseDTO confirmPaymentResponseDTO = transactionService.transferBetweenBanks(paymentRequest, confirmPaymentDTO);
        sendResponseToPaymentGateway(confirmPaymentResponseDTO, paymentRequest.getMerchantOrderId());
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", confirmPaymentResponseDTO.getAcquirerOrderId(), confirmPaymentResponseDTO.getStatus())));
        return getRedirectURL(TransactionStatus.valueOf(confirmPaymentResponseDTO.getStatus()), paymentRequest);
    }

    @Async
    public void sendResponseToPaymentGateway(ConfirmPaymentResponseDTO confirmPaymentResponseDTO, Long merchantOrderId) {
        zuulClient.confirmPayment(merchantOrderId, confirmPaymentResponseDTO);
    }

    @Override
    public Transaction payClientBank(PayRequestDTO payRequestDTO) {
        CreditCard creditCard = creditCardService.checkCreditCardDateHashedValues(payRequestDTO.getConfirmPaymentDTO().getPAN(),
                payRequestDTO.getConfirmPaymentDTO().getCardHolderName(), payRequestDTO.getConfirmPaymentDTO().getExpirationDate(), payRequestDTO.getConfirmPaymentDTO().getSecurityCode());

        TransactionStatus status = TransactionStatus.SUCCESS;
        Transaction transaction;
        if (creditCard == null) {
            status = TransactionStatus.FAILED;
            transaction = transactionService.create(payRequestDTO.getAmount(), payRequestDTO.getMerchantId(),
                    payRequestDTO.getMerchantOrderId(), payRequestDTO.getMerchantTimestamp(), payRequestDTO.getPaymentId(), payRequestDTO.getConfirmPaymentDTO().getPAN(), status);
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", transaction.getId(), transaction.getStatus())));
            return transaction;
        }

        if (!accountService.hasEnoughMoney(payRequestDTO.getAmount(), creditCard)) {
            status = TransactionStatus.FAILED;
        }

        transaction = transactionService.create(payRequestDTO.getAmount(), payRequestDTO.getMerchantId(),
                payRequestDTO.getMerchantOrderId(), payRequestDTO.getMerchantTimestamp(), payRequestDTO.getPaymentId(), payRequestDTO.getConfirmPaymentDTO().getPAN(), status);
        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", transaction.getId(), transaction.getStatus())));
        return transaction;
    }

    private String getRedirectURL(TransactionStatus status, PaymentRequest paymentRequest) {
        if (status.equals(TransactionStatus.SUCCESS)) {
            return paymentRequest.getSuccessUrl();
        }

        if (status.equals(TransactionStatus.FAILED)) {
            return paymentRequest.getFailedUrl();
        }
        return paymentRequest.getErrorUrl();
    }

    @Autowired
    public PaymentServiceImpl(PaymentRequestRepository paymentRequestRepository, CreditCardService creditCardService,
                              TransactionService transactionService, ZuulClient zuulClient, AccountService accountService, LogService logService) {
        this.paymentRequestRepository = paymentRequestRepository;
        this.creditCardService = creditCardService;
        this.transactionService = transactionService;
        this.zuulClient = zuulClient;
        this.accountService = accountService;
        this.logService = logService;
    }
}
