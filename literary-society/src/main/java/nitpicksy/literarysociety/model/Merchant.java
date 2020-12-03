package nitpicksy.literarysociety.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("MERCHANT")
public class Merchant extends User{

    @Column(nullable = false)
    private String name;

    @Column
    private boolean supportsPaymentMethods;
}
