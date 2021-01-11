package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.model.OpinionOfBetaReader;
import nitpicksy.literarysociety.repository.OpinionOfBetaReaderRepository;
import nitpicksy.literarysociety.service.OpinionOfBetaReaderService;
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
