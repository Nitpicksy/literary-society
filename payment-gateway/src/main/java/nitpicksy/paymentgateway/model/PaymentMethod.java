package nitpicksy.paymentgateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.enumeration.PaymentMethodStatus;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String commonName;

    @Column(nullable = false)
    private String URI;

    @Enumerated(EnumType.STRING)
    private PaymentMethodStatus status;

    @OneToMany
    @JoinColumn(name = "payment_method_id")
    private Set<Data> data;

    @Column
    private boolean subscription;

    @Column(unique = true, nullable = false)
    private String certificateName;

    public PaymentMethod(String name, String commonName, String URI, boolean subscription, String email) {
        this.name = name;
        this.commonName = commonName;
        this.URI = URI;
        this.status = PaymentMethodStatus.WAITING_APPROVAL;
        this.subscription = subscription;
        this.email = email;
    }
}
