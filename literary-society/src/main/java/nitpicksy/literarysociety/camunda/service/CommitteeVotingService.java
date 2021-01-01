package nitpicksy.literarysociety.camunda.service;

import nitpicksy.literarysociety.dto.request.FormSubmissionDTO;
import nitpicksy.literarysociety.repository.CommitteeOpinionRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommitteeVotingService implements JavaDelegate {

    private CommitteeOpinionRepository committeeOpinionRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        List<FormSubmissionDTO> formData = (List<FormSubmissionDTO>) execution.getVariable("formData");
        Map<String, String> map = formData.stream().collect(Collectors.toMap(FormSubmissionDTO::getFieldId, FormSubmissionDTO::getFieldValue));

        String writer = (String) execution.getVariable("writer");
        System.out.println("Writer" + writer);
//        Book book = bookRepository.findOneById(bookId);
//        book.setStatus(BookStatus.valueOf(map.get("decision")));
//        BookStatus bookStatus = book.getStatus();
//        bookRepository.save(book);
    }

    @Autowired
    public CommitteeVotingService(CommitteeOpinionRepository committeeOpinionRepository) {
        this.committeeOpinionRepository = committeeOpinionRepository;
    }


}
