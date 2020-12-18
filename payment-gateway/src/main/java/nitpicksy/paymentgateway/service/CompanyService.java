package nitpicksy.paymentgateway.service;

import nitpicksy.paymentgateway.model.Company;

public interface CompanyService {

    Company findCompanyByCommonName(String commonName);
}
