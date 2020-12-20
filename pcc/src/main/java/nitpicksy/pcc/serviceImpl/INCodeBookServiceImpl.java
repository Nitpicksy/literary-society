package nitpicksy.pcc.serviceImpl;

import nitpicksy.pcc.repository.INCodeBookRepository;
import nitpicksy.pcc.service.INCodeBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class INCodeBookServiceImpl implements INCodeBookService {

    private INCodeBookRepository inCodeBookRepository;

    @Override
    public String getBankURL(String iin) {
        return inCodeBookRepository.findByIin(iin).getUrl();
    }

    @Autowired
    public INCodeBookServiceImpl(INCodeBookRepository inCodeBookRepository) {
        this.inCodeBookRepository = inCodeBookRepository;
    }
}
