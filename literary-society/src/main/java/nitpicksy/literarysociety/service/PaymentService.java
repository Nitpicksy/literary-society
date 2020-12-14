package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.User;

import java.util.List;
import java.util.Set;

public interface PaymentService {

    String proceedToPayment(Set<Book> bookList, User user);
}
