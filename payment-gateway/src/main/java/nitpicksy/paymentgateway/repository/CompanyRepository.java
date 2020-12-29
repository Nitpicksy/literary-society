package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.enumeration.CompanyStatus;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;
import nitpicksy.paymentgateway.model.Company;
import nitpicksy.paymentgateway.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByCompanyName(String companyName);

    Company findByCommonName(String commonName);

    List<Company> findByStatusNot(CompanyStatus status);

}
