package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.enumeration.TransactionType;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.model.User;
import nitpicksy.literarysociety.repository.TransactionRepository;
import nitpicksy.literarysociety.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    @Override
    public Transaction create(TransactionStatus status, TransactionType type, User buyer, Double amount, Set<Book> orderedBooks, Merchant merchant) {
        return transactionRepository.save(new Transaction(status,type,buyer,amount,orderedBooks,merchant));
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findOneById(id);
    }


    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
}
