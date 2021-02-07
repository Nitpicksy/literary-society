package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.dto.request.LiterarySocietyOrderRequestDTO;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.Set;

public interface PaymentService {

    String proceedToBookPayment(Set<Book> bookList, User user);

    String proceedToMembershipPayment(User user);

    void handlePayment(LiterarySocietyOrderRequestDTO dto) throws NoSuchAlgorithmException;

    void synchronizeTransactions();

}
