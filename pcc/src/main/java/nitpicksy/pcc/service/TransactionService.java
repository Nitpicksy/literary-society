package nitpicksy.pcc.service;

import nitpicksy.pcc.dto.PCCRequestDTO;
import nitpicksy.pcc.dto.PayResponseDTO;

public interface TransactionService {

    PayResponseDTO pay(PCCRequestDTO pccRequestDTO);
}
