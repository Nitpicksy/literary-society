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
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //14 - 19 digits
    //prvih sest cifara identifikuju banku
    @Column(nullable = false, unique = true)
    @Convert(converter = CryptoStringConverter.class)
    private String pan;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String securityCode;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String cardHolderName;

    @Column(nullable = false)
    private LocalDate expirationDate;

}
