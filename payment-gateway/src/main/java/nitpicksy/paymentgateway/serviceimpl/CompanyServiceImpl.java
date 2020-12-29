package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.dto.both.PaymentMethodDTO;
import nitpicksy.paymentgateway.enumeration.CompanyStatus;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import nitpicksy.paymentgateway.repository.CompanyRepository;
import nitpicksy.paymentgateway.repository.PaymentMethodRepository;
import nitpicksy.paymentgateway.service.CompanyService;
import nitpicksy.paymentgateway.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    private PaymentMethodRepository paymentMethodRepository;

    @Override
    public Company addCompany(Company company, List<PaymentMethodDTO> paymentMethodDTOs) {
        if (companyRepository.findByCompanyName(company.getCompanyName()) != null
                || companyRepository.findByCommonName(company.getCommonName()) != null) {
            throw new InvalidDataException("Company with the same name already exists.", HttpStatus.BAD_REQUEST);
        }
        List<Long> ids = paymentMethodDTOs.stream().map(PaymentMethodDTO::getId).collect(Collectors.toList());
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByIdIn(ids);
        if (paymentMethods.isEmpty()) {
            throw new InvalidDataException("Non-existing payment methods chosen", HttpStatus.BAD_REQUEST);
        }

        company.setStatus(CompanyStatus.WAITING_APPROVAL);
        company.setPaymentMethods(new HashSet<>(paymentMethods));

        return companyRepository.save(company);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findByStatusNot(CompanyStatus.REJECTED);
    }

    @Override
    public Company findCompanyByCommonName(String commonName) {
        return companyRepository.findByCommonName(commonName);
    }

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository, PaymentMethodRepository paymentMethodRepository) {
        this.companyRepository = companyRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }
}
