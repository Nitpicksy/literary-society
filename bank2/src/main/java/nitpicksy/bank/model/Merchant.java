package nitpicksy.bank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.bank.converter.CryptoStringConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Double balance;

    @Column(nullable = false, unique = true)
    @Convert(converter = CryptoStringConverter.class)
    private String merchantId;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String merchantPassword;
}
