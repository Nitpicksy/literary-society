package nitpicksy.literarysociety.elasticsearch.mapper;

import java.util.List;
import java.util.Set;

public interface MapperInterfaceElasticSearch<T, U> {

    T entityToInfo(U entity);

    List<T> entitiesToInfos(Set<U> entities);
}
