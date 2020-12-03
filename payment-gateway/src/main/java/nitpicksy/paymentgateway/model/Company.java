package nitpicksy.paymentgateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String URI;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "success_url", nullable = false)
    private String successURL;

    @Column(name = "error_url", nullable = false)
    private String errorURL;

    @Column(name ="failed_url", nullable = false)
    private String failedURL;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_payment_methods",
            joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id", referencedColumnName = "id"))
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "company_id")
    private Set<Merchant> merchant;
}
