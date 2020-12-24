package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.model.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataMapper implements MapperInterface<Data, PaymentDataDTO> {

    private final ModelMapper modelMapper;

    @Override
    public Data toEntity(PaymentDataDTO dto) {
        return modelMapper.map(dto, Data.class);
    }

    @Override
    public PaymentDataDTO toDto(Data entity) {
        return modelMapper.map(entity, PaymentDataDTO.class);
    }

    public Set<Data> convertList(List<PaymentDataDTO> paymentDataDTOList){
        Set<Data> data = new HashSet<>();
        for (PaymentDataDTO paymentDataDTO:paymentDataDTOList) {
            data.add(toEntity(paymentDataDTO));
        }
        return data;
    }

    public List<PaymentDataDTO> convertToDto(Set<Data> dataDTOList){
        List<PaymentDataDTO> paymentDataDTO = new ArrayList<>();
        for (Data data:dataDTOList) {
            paymentDataDTO.add(toDto(data));
        }
        return paymentDataDTO;
    }
    @Autowired
    public DataMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

}
