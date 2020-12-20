package nitpicksy.paymentgateway.serviceimpl;

import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.repository.CompanyRepository;
import nitpicksy.paymentgateway.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;

    @Override
    public Company findCompanyByCommonName(String commonName) {
        return companyRepository.findByCommonName(commonName);
    }

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
}
