package nitpicksy.bank.serviceImpl;

import nitpicksy.bank.client.ZuulClient;
import nitpicksy.bank.dto.request.ConfirmPaymentDTO;
import nitpicksy.bank.dto.request.PCCRequestDTO;
import nitpicksy.bank.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.bank.dto.response.PCCResponseDTO;
import nitpicksy.bank.enumeration.TransactionStatus;
import nitpicksy.bank.exceptionHandler.InvalidDataException;
import nitpicksy.bank.mapper.PCCRequestMapper;
import nitpicksy.bank.model.CreditCard;
import nitpicksy.bank.model.Log;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.model.Transaction;
import nitpicksy.bank.repository.TransactionRepository;
import nitpicksy.bank.service.AccountService;
import nitpicksy.bank.service.LogService;
import nitpicksy.bank.service.MerchantService;
import nitpicksy.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private HashValueServiceImpl hashValueServiceImpl;

    private TransactionRepository transactionRepository;

    private AccountService accountService;

    private MerchantService merchantService;

    private PCCRequestMapper pccRequestMapper;

    private ZuulClient pccClient;

    private LogService logService;

    @Override
    public Transaction transferInsideBank(PaymentRequest paymentRequest, CreditCard creditCard)  {
        Transaction transaction = create(paymentRequest,creditCard.getPan());

        try {
            transfer(creditCard,paymentRequest.getMerchantId(),paymentRequest.getAmount());
        } catch (NoSuchAlgorithmException e) {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", String.format("Hash algorithm does not exist.")));
            return setTransactionStatus(transaction,TransactionStatus.ERROR);
        }catch (InvalidDataException e){
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", String.format("User with credit card %s doesn't have enough money.", creditCard.getId())));
            return setTransactionStatus(transaction,TransactionStatus.FAILED);
        }

        return setTransactionStatus(transaction,TransactionStatus.SUCCESS);
    }

    @Override
    public Transaction findByMerchantOrderId(Long id) {
        return transactionRepository.findByMerchantOrderId(id);
    }

    @Override
    public ConfirmPaymentResponseDTO transferBetweenBanks(PaymentRequest paymentRequest, ConfirmPaymentDTO confirmPaymentDTO)  {
        Transaction transaction = null;
        ConfirmPaymentResponseDTO confirmPaymentResponseDTO = new ConfirmPaymentResponseDTO(paymentRequest.getId());
        try {
            transaction = create(paymentRequest,hashValueServiceImpl.getHashValue(confirmPaymentDTO.getPAN()));
        } catch (NoSuchAlgorithmException e) {
            setTransactionStatus(transaction, TransactionStatus.ERROR);
            confirmPaymentResponseDTO.setStatus(TransactionStatus.ERROR.toString());
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", String.format("Transaction %s is failed. Hash algorithm does not exist.", transaction.getId())));
            return confirmPaymentResponseDTO;
        }
        transactionRepository.save(transaction);
        try {
            PCCRequestDTO pccRequestDTO = pccRequestMapper.toDTO(transaction, confirmPaymentDTO);
            PCCResponseDTO response = pccClient.pay(pccRequestDTO);
            Transaction createdTransaction = setTransactionStatus(transaction,response.getStatus());
            if(createdTransaction.getStatus().equals(TransactionStatus.SUCCESS)){
                merchantService.transferMoneyToMerchant(transaction.getMerchantId(),transaction.getAmount());
            }
            confirmPaymentResponseDTO.setAcquirerOrderId(response.getAcquirerOrderId());
            confirmPaymentResponseDTO.setAcquirerTimestamp(response.getAcquirerTimestamp());
            confirmPaymentResponseDTO.setStatus(response.getStatus().toString());
            return confirmPaymentResponseDTO;
        } catch (NoSuchAlgorithmException | IllegalArgumentException e) {
            setTransactionStatus(transaction, TransactionStatus.ERROR);
            confirmPaymentResponseDTO.setStatus(TransactionStatus.ERROR.toString());
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "TRA", String.format("Transaction %s is failed. Hash algorithm does not exist.", transaction.getId())));
            return confirmPaymentResponseDTO;
        }
    }

    @Override
    public Transaction create(Double amount, String merchantId, Long merchantOrderId, Timestamp merchantTimestamp, Long paymentId, String pan, TransactionStatus status) {
        Transaction transaction =  new Transaction(amount,merchantId, merchantOrderId,
                merchantTimestamp,paymentId,pan,status);
        return transactionRepository.save(transaction);
    }

    private Transaction create(PaymentRequest paymentRequest, String pan)  {
        return new Transaction(paymentRequest.getAmount(), paymentRequest.getMerchantId(), paymentRequest.getMerchantOrderId(),
                paymentRequest.getMerchantTimestamp(),pan,paymentRequest.getId());
    }

    @Transactional
    public void transfer(CreditCard buyer, String merchantId, Double amount) throws NoSuchAlgorithmException {
        if(!accountService.hasEnoughMoney(amount,buyer)){
            throw new InvalidDataException("You don't have enough money.", HttpStatus.BAD_REQUEST);
        }
        merchantService.transferMoneyToMerchant(merchantId,amount);
    }

    private Transaction setTransactionStatus(Transaction transaction, TransactionStatus status){
        transaction.setStatus(status);
        return transactionRepository.save(transaction);
    }


    @Autowired
    public TransactionServiceImpl(HashValueServiceImpl hashValueServiceImpl, TransactionRepository transactionRepository,AccountService accountService,
                                  MerchantService merchantService,PCCRequestMapper pccRequestMapper,ZuulClient pccClient,LogService logService) {
        this.hashValueServiceImpl = hashValueServiceImpl;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
        this.merchantService = merchantService;
        this.pccRequestMapper = pccRequestMapper;
        this.pccClient = pccClient;
        this.logService=logService;
    }
}