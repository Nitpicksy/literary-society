package nitpicksy.paymentgateway.mapper;

import nitpicksy.paymentgateway.dto.request.PaymentDataDTO;
import nitpicksy.paymentgateway.model.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        return null;
    }

    public Set<Data> convertList(List<PaymentDataDTO> paymentDataDTOList){
        Set<Data> data = new HashSet<>();
        for (PaymentDataDTO paymentDataDTO:paymentDataDTOList) {
            data.add(toEntity(paymentDataDTO));
        }
        return data;
    }
    @Autowired
    public DataMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


}
