package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.model.OpinionOfBetaReader;
import nitpicksy.literarysociety2.repository.OpinionOfBetaReaderRepository;
import nitpicksy.literarysociety2.service.OpinionOfBetaReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpinionOfBetaReaderServiceImpl implements OpinionOfBetaReaderService {

    private OpinionOfBetaReaderRepository opinionOfBetaReaderRepository;

    @Override
    public List<OpinionOfBetaReader> findByBookId(Long bookId) {
        return opinionOfBetaReaderRepository.findByBookId(bookId);
    }

    @Autowired
    public OpinionOfBetaReaderServiceImpl(OpinionOfBetaReaderRepository opinionOfBetaReaderRepository) {
        this.opinionOfBetaReaderRepository = opinionOfBetaReaderRepository;
    }
}
