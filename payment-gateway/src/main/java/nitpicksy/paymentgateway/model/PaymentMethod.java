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

    @Column(unique = true, nullable = false)
    private String commonName;

    @Column(nullable = false)
    private String URI;

    @Enumerated(EnumType.STRING)
    private PaymentMethodStatus status;

    @OneToMany
    @JoinColumn(name = "payment_method_id")
    private Set<Data> data;
}
