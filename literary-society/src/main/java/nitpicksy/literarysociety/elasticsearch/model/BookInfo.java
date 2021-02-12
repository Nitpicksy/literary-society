package nitpicksy.literarysociety.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "book", type = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookInfo {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String title;

    @Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String writers;

    @Field(type = FieldType.Text, analyzer = "serbian-analyzer", searchAnalyzer = "serbian-analyzer")
    private String content;

    @Field(type = FieldType.Nested)
    private GenreInfo genreInfo;

    @Field(type = FieldType.Boolean)
    private boolean openAccess;

    public BookInfo(Long id, String title, String writers, boolean openAccess,GenreInfo genreInfo) {
        this.id = id;
        this.title = title;
        this.writers = writers;
        this.openAccess = openAccess;
        this.genreInfo = genreInfo;
    }
}

