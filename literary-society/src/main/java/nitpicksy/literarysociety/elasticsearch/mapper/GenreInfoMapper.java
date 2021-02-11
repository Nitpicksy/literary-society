package nitpicksy.literarysociety.elasticsearch.mapper;

import nitpicksy.literarysociety.elasticsearch.model.GenreInfo;
import nitpicksy.literarysociety.model.Genre;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GenreInfoMapper implements MapperInterfaceElasticSearch<GenreInfo, Genre> {

    @Override
    public GenreInfo entityToInfo(Genre entity) {
        return new GenreInfo(entity.getId(), entity.getName());
    }

    @Override
    public List<GenreInfo> entitiesToInfos(Set<Genre> entities) {
        List<GenreInfo> genreInfos = new ArrayList<>();
        for (Genre genre: entities) {
            genreInfos.add(entityToInfo(genre));
        }
        return genreInfos;
    }
}
