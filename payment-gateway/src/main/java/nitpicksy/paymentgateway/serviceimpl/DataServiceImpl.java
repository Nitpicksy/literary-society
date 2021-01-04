package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.model.Data;
import nitpicksy.paymentgateway.repository.DataRepository;
import nitpicksy.paymentgateway.service.DataService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DataServiceImpl implements DataService {

    private DataRepository dataRepository;

    @Override
    public Set<Data> create(Set<Data> listData) {
        Set<Data> createdData = new HashSet<>();
        for (Data data:listData) {
            createdData.add(dataRepository.saveAndFlush(data));
        }
        return createdData;
    }

    @Override
    public Data findById(Long id) {
        return dataRepository.findById(id).get();
    }

    public DataServiceImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
}
