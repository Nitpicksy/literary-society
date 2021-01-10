package nitpicksy.bank.mapper;

import nitpicksy.bank.dto.response.AccountResponseDTO;
import nitpicksy.bank.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountResponseMapper implements MapperInterface<Account, AccountResponseDTO> {

    private ModelMapper modelMapper;

    @Override
    public Account toEntity(AccountResponseDTO dto) {
        return modelMapper.map(dto, Account.class);
    }

    @Override
    public AccountResponseDTO toDto(Account entity) {
        return modelMapper.map(entity, AccountResponseDTO.class);
    }

    @Autowired
    public AccountResponseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}