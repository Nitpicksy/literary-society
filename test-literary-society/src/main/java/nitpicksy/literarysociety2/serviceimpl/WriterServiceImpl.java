package nitpicksy.literarysociety2.serviceimpl;

import nitpicksy.literarysociety2.model.Writer;
import nitpicksy.literarysociety2.repository.WriterRepository;
import nitpicksy.literarysociety2.service.WriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WriterServiceImpl implements WriterService {

    private WriterRepository writerRepository;

    @Override
    public Writer findByUsername(String username) {
        return writerRepository.findByUsername(username);
    }

    @Override
    public Writer findByEmail(String email) {
        return writerRepository.findByEmail(email);
    }

    @Override
    public Writer findById(Long id) {
        return writerRepository.findOneById(id);
    }

    @Autowired
    public WriterServiceImpl(WriterRepository writerRepository) {
        this.writerRepository = writerRepository;
    }

}
