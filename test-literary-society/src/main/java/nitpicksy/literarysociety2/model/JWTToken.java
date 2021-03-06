package nitpicksy.literarysociety2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nitpicksy.literarysociety2.converter.CryptoStringConverter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    @Convert(converter = CryptoStringConverter.class)
    private String token;

    @Column(nullable = false, length = 500)
    @Convert(converter = CryptoStringConverter.class)
    private String refreshToken;

    public JWTToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}

