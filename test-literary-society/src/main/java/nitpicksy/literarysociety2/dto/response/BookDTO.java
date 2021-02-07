package nitpicksy.literarysociety2.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class BookDTO {

    @Positive(message = "Id must be positive.")
    @NotNull(message = "Id is null.")
    private Long id;

    @NotBlank(message = "Title is empty.")
    private String title;

    @NotBlank(message = "Writers' names are empty.")
    private String writersNames;

    @NotNull(message = "Price is null.")
    @PositiveOrZero(message = "Price is not a positive number.")
    private Double price;

    @NotNull(message = "Discount is null.")
    @PositiveOrZero(message = "Discount is not a positive number.")
    private Integer discount;

    @NotBlank(message = "Merchant's name is empty.")
    private String merchantName;

    @NotBlank(message = "Image data is empty.")
    @Pattern(regexp = "^(data:image/[^;]+;base64[^\"]+)$", message = "Image data is not valid.")
    private String imageData;

}
