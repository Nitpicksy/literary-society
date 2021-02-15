package nitpicksy.literarysociety.serviceimpl;

import nitpicksy.literarysociety.enumeration.UserStatus;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.repository.ReaderRepository;
import nitpicksy.literarysociety.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    private ReaderRepository readerRepository;

    @Override
    public List<Reader> findByIds(List<Long> ids) {
        return readerRepository.findByIdIn(ids);
    }

    @Override
    public Reader save(Reader reader) {
        return readerRepository.save(reader);
    }

    @Override
    public List<Reader> findBetaReaders(Long genreId) {
        return readerRepository.findByIsBetaReaderAndBetaReaderGenresIdAndStatus(true, genreId, UserStatus.ACTIVE);
    }

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }


}
