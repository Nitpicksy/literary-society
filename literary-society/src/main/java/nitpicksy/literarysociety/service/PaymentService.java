package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.dto.request.LiterarySocietyOrderRequestDTO;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.User;

import java.util.Set;

public interface PaymentService {

    String proceedToPayment(Set<Book> bookList, User user);

    void handlePayment(LiterarySocietyOrderRequestDTO dto);
}
