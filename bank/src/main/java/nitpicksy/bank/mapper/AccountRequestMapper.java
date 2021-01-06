package nitpicksy.bank.mapper;

import nitpicksy.bank.dto.request.AccountRequestDTO;
import nitpicksy.bank.dto.response.AccountResponseDTO;
import nitpicksy.bank.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRequestMapper implements MapperInterface<Account, AccountRequestDTO> {

    private ModelMapper modelMapper;

    @Override
    public Account toEntity(AccountRequestDTO dto) {
        return modelMapper.map(dto, Account.class);
    }

    @Override
    public AccountRequestDTO toDto(Account entity) {
        return modelMapper.map(entity, AccountRequestDTO.class);
    }

    @Autowired
    public AccountRequestMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}