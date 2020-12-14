package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.enumeration.TransactionStatus;
import nitpicksy.literarysociety.enumeration.TransactionType;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.Merchant;
import nitpicksy.literarysociety.model.Transaction;
import nitpicksy.literarysociety.model.User;

import java.util.Set;

public interface TransactionService {

    Transaction create(TransactionStatus status, TransactionType type, User buyer, Double amount, Set<Book> orderedBooks, Merchant merchant);

    Transaction save(Transaction transaction);
}
