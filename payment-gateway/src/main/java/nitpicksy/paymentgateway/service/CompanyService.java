package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.model.Company;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {

    Company addCompany(Company company, List<PaymentMethodDTO> paymentMethods);

    Company changeStatus(Long id, String status);

    List<Company> findAll();

    Company findCompanyByCommonName(String commonName);

    String getToken();

}
