package nitpicksy.paypalservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.paypalservice.converter.CryptoStringConverter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String planId;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String merchantClientId;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String merchantClientSecret;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private String productCategory;

    @Column(nullable = false)
    private String planName;

    @Column(nullable = false)
    private String planDescription;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String frequencyUnit;

    @Column(nullable = false)
    private Integer frequencyCount;

    @Column(nullable = false)
    private String successURL;

    @Column(nullable = false)
    private String cancelURL;

}
