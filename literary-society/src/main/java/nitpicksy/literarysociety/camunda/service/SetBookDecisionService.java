package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.enumeration.BookStatus;
import nitpicksy.literarysociety.model.Book;
import nitpicksy.literarysociety.model.PlagiarismComplaint;
import nitpicksy.literarysociety.repository.BookRepository;
import nitpicksy.literarysociety.repository.PlagiarismComplaintRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetBookDecisionService implements JavaDelegate {

    private PlagiarismComplaintRepository plagiarismComplaintRepository;
    private BookRepository bookRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        Long plagiarismId = Long.valueOf((String) execution.getVariable("plagiarismId"));
        PlagiarismComplaint complaint = plagiarismComplaintRepository.findById(plagiarismId).orElse(null);

        boolean plagiarism = (boolean) execution.getVariable("plagiarism");

        if (plagiarism) {
            assert complaint != null;
            Book book = complaint.getReportedBook();
            book.setStatus(BookStatus.NOT_ORIGINAL);
            bookRepository.save(book);
        }
    }

    @Autowired
    public SetBookDecisionService(PlagiarismComplaintRepository plagiarismComplaintRepository, BookRepository bookRepository) {
        this.plagiarismComplaintRepository = plagiarismComplaintRepository;
        this.bookRepository = bookRepository;
    }
}
