package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.model.OpinionOfEditor;
import nitpicksy.literarysociety2.repository.OpinionOfEditorRepository;
import nitpicksy.literarysociety2.service.OpinionOfEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpinionOfEditorServiceImpl implements OpinionOfEditorService {

    private OpinionOfEditorRepository opinionOfEditorRepository;

    @Override
    public OpinionOfEditor findNewestByBookId(Long bookId) {
        return opinionOfEditorRepository.findByBookIdOrderByCreatedDesc(bookId).get(0);
    }

    @Autowired
    public OpinionOfEditorServiceImpl(OpinionOfEditorRepository opinionOfEditorRepository) {
        this.opinionOfEditorRepository = opinionOfEditorRepository;
    }
}
