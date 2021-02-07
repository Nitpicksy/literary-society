package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.enumeration.TransactionStatus;
import nitpicksy.literarysociety2.enumeration.TransactionType;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.Merchant;
import nitpicksy.literarysociety2.model.Transaction;
import nitpicksy.literarysociety2.model.User;

import java.util.List;
import java.util.Set;

public interface TransactionService {

    Transaction create(TransactionStatus status, TransactionType type, User buyer, Double amount, Set<Book> orderedBooks, Merchant merchant);

    Transaction save(Transaction transaction);

    Transaction findById(Long id);

    List<Transaction> all();
}
