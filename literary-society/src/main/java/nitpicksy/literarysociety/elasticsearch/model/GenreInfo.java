package nitpicksy.literarysociety.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "genre", type = "genre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreInfo {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text,analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String name;

}
