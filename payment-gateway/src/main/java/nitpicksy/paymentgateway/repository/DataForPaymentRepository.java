package nitpicksy.paymentgateway.repository;

import nitpicksy.paymentgateway.model.DataForPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataForPaymentRepository extends JpaRepository<DataForPayment, Long> {

    @Query(value = "select dfp.* from data_for_payment dfp where dfp.merchant_id=:merchantId and dfp.payment_method_id=:paymentId", nativeQuery = true)
    List<DataForPayment> findDataForPaymentByMerchantAndPaymentMethod(@Param("merchantId") Long merchantId, @Param("paymentId") Long paymentId);

    List<DataForPayment> findByMerchantId(Long id);
}
