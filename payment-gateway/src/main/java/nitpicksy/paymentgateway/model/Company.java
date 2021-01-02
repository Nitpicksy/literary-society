package nitpicksy.paymentgateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paymentgateway.enumeration.AdminStatus;
import nitpicksy.paymentgateway.enumeration.CompanyStatus;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String URI;

    @Column(nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String commonName;

    @Column(name = "success_url", nullable = false)
    private String successURL;

    @Column(name = "error_url", nullable = false)
    private String errorURL;

    @Column(name = "failed_url", nullable = false)
    private String failedURL;

    @Column(unique = true, nullable = false)
    private String certificateName;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @Enumerated(EnumType.STRING)
    private CompanyStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "company_payment_methods",
            joinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id", referencedColumnName = "id"))
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(mappedBy = "company")
    private Set<Merchant> merchant;

    @Column
    private boolean enabled;

    @Column
    private Timestamp lastPasswordResetDate = new Timestamp(DateTime.now().getMillis());

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        this.role.getPermissions().forEach(p -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(p.getName());
            authorities.add(authority);
        });

        return authorities;
    }


    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return commonName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (this.getStatus() == CompanyStatus.APPROVED);
    }

}
