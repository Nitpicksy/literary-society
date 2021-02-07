package nitpicksy.literarysociety2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
