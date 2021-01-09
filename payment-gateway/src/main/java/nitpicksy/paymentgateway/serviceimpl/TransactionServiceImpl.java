package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.model.Transaction;
import nitpicksy.paymentgateway.repository.TransactionRepository;
import nitpicksy.paymentgateway.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll(Long companyId) {
        return transactionRepository.findByCompanyId(companyId);
    }

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
