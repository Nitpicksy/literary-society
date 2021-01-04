package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import nitpicksy.literarysociety.model.OpinionOfEditor;
import nitpicksy.literarysociety.repository.OpinionOfBetaReaderRepository;
import nitpicksy.literarysociety.repository.OpinionOfEditorRepository;
import nitpicksy.literarysociety.service.OpinionOfBetaReaderService;
import nitpicksy.literarysociety.service.OpinionOfEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
