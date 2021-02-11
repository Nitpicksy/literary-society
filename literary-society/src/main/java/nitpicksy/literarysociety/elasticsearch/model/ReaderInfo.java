package nitpicksy.literarysociety.elasticsearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Document(indexName = "reader", type = "reader")
@Getter
@Setter
@NoArgsConstructor
public class ReaderInfo {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String country;

    @Field(type = FieldType.Nested)
    private List<GenreInfo> genre;

    @GeoPointField
    private GeoPoint geoPoint;
}

