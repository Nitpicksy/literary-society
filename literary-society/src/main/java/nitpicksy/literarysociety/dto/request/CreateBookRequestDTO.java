package nitpicksy.literarysociety.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CreateBookRequestDTO {

    @NotBlank(message = "Writer name is empty")
    private String writersNames;

    @NotBlank(message = "Title is empty")
    private String title;

    @NotBlank(message = "Synopsis is empty")
    private String synopsis;

    @Positive
    @NotNull
    private Long genreId;

    @NotBlank(message = "ISBN is empty")
    @Size(max = 13)
    private String ISBN;

    @Positive
    @NotNull
    private Integer numberOfPages;

    @NotBlank(message = "Publisher city")
    private String publisherCity;

    @NotBlank(message = "Publication date is empty")
    private String publicationDate;

    @NotBlank(message = "Publisher is empty")
    private String publisher;

    @Positive
    @NotNull
    private Double price;

    @NotNull
    @Max(100)
    @Min(1)
    private Integer discount;
}
