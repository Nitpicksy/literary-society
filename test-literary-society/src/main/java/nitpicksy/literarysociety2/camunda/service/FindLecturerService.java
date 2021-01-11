package nitpicksy.literarysociety2.camunda.service;

import nitpicksy.literarysociety2.constants.RoleConstants;
import nitpicksy.literarysociety2.enumeration.UserStatus;
import nitpicksy.literarysociety2.model.Book;
import nitpicksy.literarysociety2.model.User;
import nitpicksy.literarysociety2.repository.BookRepository;
import nitpicksy.literarysociety2.service.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class FindLecturerService implements JavaDelegate {


    private UserService userService;

    private BookRepository bookRepository;

    @Override
    public void execute(DelegateExecution execution)  {

        List<User> lecturers = userService.findAllWithRoleAndStatus(RoleConstants.ROLE_LECTURER, UserStatus.ACTIVE);
        User chosenLecturer = lecturers.get(new Random().nextInt(lecturers.size()));

        Long bookId = Long.valueOf((String) execution.getVariable("bookId"));
        Book book = bookRepository.findOneById(bookId);
        book.setLecturer(chosenLecturer);
        bookRepository.save(book);

        execution.setVariable("lecturer", chosenLecturer.getUsername());

        System.out.println("Lecturer: " + chosenLecturer.getUsername());

    }

    @Autowired
    public FindLecturerService(UserService userService, BookRepository bookRepository) {

        this.userService = userService;
        this.bookRepository = bookRepository;
    }
}
