package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.enumeration.TransactionStatus;
import nitpicksy.literarysociety2.enumeration.TransactionType;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Merchant;
import nitpicksy.literarysociety2.model.Transaction;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.repository.TransactionRepository;
import nitpicksy.literarysociety2.service.TransactionService;
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
