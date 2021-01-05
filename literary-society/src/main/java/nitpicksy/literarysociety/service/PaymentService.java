package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.request.LiterarySocietyOrderRequestDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.User;

import java.util.Set;

public interface PaymentService {

    String proceedToBookPayment(Set<Book> bookList, User user);

    String proceedToMembershipPayment(User user);

    void handlePayment(LiterarySocietyOrderRequestDTO dto);
}
