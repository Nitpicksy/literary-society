package nitpicksy.bank.mapper;


import nitpicksy.bank.dto.request.PaymentRequestDTO;
import nitpicksy.bank.model.Log;
import nitpicksy.bank.model.PaymentRequest;
import nitpicksy.bank.service.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

@Component
public class PaymentRequestMapper implements MapperInterface<PaymentRequest, PaymentRequestDTO> {

    private ModelMapper modelMapper;

    @Override
    public PaymentRequest toEntity(PaymentRequestDTO dto) throws NoSuchAlgorithmException {
        PaymentRequest request =  modelMapper.map(dto, PaymentRequest.class);
        request.setMerchantTimestamp(Timestamp.valueOf(dto.getMerchantTimestamp()));
        request.setMerchantId(getHashValue(dto.getMerchantId()));
        return request;
    }

    @Override
    public PaymentRequestDTO toDto(PaymentRequest entity) {
        PaymentRequestDTO dto =  modelMapper.map(entity, PaymentRequestDTO.class);
        dto.setMerchantTimestamp(entity.getMerchantTimestamp().toString());
        return dto;
    }

    private String getHashValue(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }


    @Autowired
    public PaymentRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
