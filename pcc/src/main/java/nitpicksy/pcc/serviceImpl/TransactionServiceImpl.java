package nitpicksy.pcc.serviceImpl;

import nitpicksy.pcc.dto.PCCRequestDTO;
import nitpicksy.pcc.dto.PayRequestDTO;
import nitpicksy.pcc.dto.PayResponseDTO;
import nitpicksy.pcc.enumeration.TransactionStatus;
import nitpicksy.pcc.model.Log;
import nitpicksy.pcc.model.Transaction;
import nitpicksy.pcc.repository.TransactionRepository;
import nitpicksy.pcc.service.INCodeBookService;
import nitpicksy.pcc.service.LogService;
import nitpicksy.pcc.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeoutException;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final String CLASS_PATH = this.getClass().getCanonicalName();

    private final String CLASS_NAME = this.getClass().getSimpleName();

    private TransactionRepository transactionRepository;

    private INCodeBookService inCodeBookService;

    private RestTemplate restTemplate;

    private LogService logService;

    @Override
    public PayResponseDTO pay(PCCRequestDTO pccRequestDTO) {
        String url = inCodeBookService.getBankURL(pccRequestDTO.getIin());
        Transaction transaction =  new Transaction(pccRequestDTO.getAcquirerOrderId(),pccRequestDTO.getAcquirerTimestamp(),
                pccRequestDTO.getAmount());
        try{
            PayResponseDTO payResponseDTO = sendRequestToIssuerBank(pccRequestDTO,url);
            transaction.setIssuerOrderId(payResponseDTO.getIssuerOrderId());
            transaction.setIssuerTimestamp(payResponseDTO.getIssuerTimestamp());
            transaction.setStatus(payResponseDTO.getStatus());
            transactionRepository.save(transaction);
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s", transaction.getId(),transaction.getStatus())));
            return payResponseDTO;
        }catch (RestClientException ex){
            transaction.setStatus(TransactionStatus.ERROR);
            transactionRepository.save(transaction);
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONFPAY", String.format("Transaction %s is created.Status of this transaction is %s. Communication between pcc and bank doesn't work.", transaction.getId(),transaction.getStatus())));
        }
        return null;
    }

    private PayResponseDTO sendRequestToIssuerBank(PCCRequestDTO pccRequestDTO,String url) throws RestClientException {
        PayRequestDTO payRequestDTO = new PayRequestDTO(pccRequestDTO.getAcquirerOrderId(),pccRequestDTO.getAcquirerTimestamp(),
                pccRequestDTO.getAmount(),pccRequestDTO.getConfirmPaymentDTO(),pccRequestDTO.getMerchantId(),
                pccRequestDTO.getMerchantOrderId(), pccRequestDTO.getMerchantTimestamp(),pccRequestDTO.getPaymentId());

        ResponseEntity<PayResponseDTO> responseEntity = restTemplate.exchange(url + "/accounts/pay", HttpMethod.POST,
                new HttpEntity<>(payRequestDTO), PayResponseDTO.class);
       return responseEntity.getBody();
    }
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,INCodeBookService inCodeBookService,RestTemplate restTemplate,
                                  LogService logService) {
        this.transactionRepository = transactionRepository;
        this.inCodeBookService = inCodeBookService;
        this.restTemplate = restTemplate;
        this.logService = logService;
    }
}
