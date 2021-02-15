package nitpicksy.literarysociety.elastic.serviceimpl;

import com.byteowls.jopencage.model.JOpenCageLatLng;
import nitpicksy.literarysociety.elastic.model.BetaReaderIndexingUnit;
import nitpicksy.literarysociety.elastic.model.GenreIndexingUnit;
import nitpicksy.literarysociety.elastic.repository.BetaReaderIndexRepository;
import nitpicksy.literarysociety.elastic.service.BetaReaderIndexService;
import nitpicksy.literarysociety.elastic.service.GenreIndexService;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.Reader;
import nitpicksy.literarysociety.utils.LocationProvider;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BetaReaderIndexServiceImpl implements BetaReaderIndexService {

    private ElasticsearchTemplate elasticsearchTemplate;

    private BetaReaderIndexRepository betaReaderIndexRepository;

    private GenreIndexService genreIndexService;

    @Override
    public BetaReaderIndexingUnit addBetaReader(Reader betaReader) {
        BetaReaderIndexingUnit betaReaderIdxUnit = new BetaReaderIndexingUnit();
        betaReaderIdxUnit.setId(betaReader.getUserId());
        betaReaderIdxUnit.setName(betaReader.getFirstName() + " " + betaReader.getLastName());

        String cityAndCountry = betaReader.getCity() + ", " + betaReader.getCountry();
        betaReaderIdxUnit.setCityAndCountry(cityAndCountry);

        JOpenCageLatLng coordinates = LocationProvider.getCoordinates(cityAndCountry);
        betaReaderIdxUnit.setGeoPoint(new GeoPoint(coordinates.getLat(), coordinates.getLng()));
        
        List<Long> genreIds = betaReader.getBetaReaderGenres().stream().map(Genre::getId).collect(Collectors.toList());
        betaReaderIdxUnit.setGenres(genreIndexService.findByIds(genreIds));

        return betaReaderIndexRepository.save(betaReaderIdxUnit);
    }

    @Override
    public List<BetaReaderIndexingUnit> filterByGeoLocation(Double lat, Double lon) {
//        QueryBuilder filter = QueryBuilders.geoDistanceQuery("geoPoint")
//                .point(lat, lon)
//                .distance(100, DistanceUnit.KILOMETERS);
//
//        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
//        boolQuery.mustNot(filter);
//
//        SearchQuery theQuery = new NativeSearchQueryBuilder().withQuery(boolQuery).build();
//        return elasticsearchTemplate.queryForList(theQuery, BetaReaderIndexingUnit.class);
        return null;
    }

    @Override
    public List<BetaReaderIndexingUnit> filterByGenreAndGeoLocation(String genreName, Double lat, Double lon) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        QueryBuilder genreFilter = QueryBuilders.termQuery("genres.name", genreName);
        NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("genres",
                QueryBuilders.boolQuery().must(genreFilter), ScoreMode.None);
        boolQuery.must(nestedQuery);

        QueryBuilder geoLocationFilter = QueryBuilders.geoDistanceQuery("geoPoint")
                .point(lat, lon)
                .distance(100, DistanceUnit.KILOMETERS);
        boolQuery.mustNot(geoLocationFilter);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQuery).build();
        return elasticsearchTemplate.queryForList(searchQuery, BetaReaderIndexingUnit.class);
    }

    @Autowired
    public BetaReaderIndexServiceImpl(ElasticsearchTemplate elasticsearchTemplate, BetaReaderIndexRepository betaReaderIndexRepository,
                                      GenreIndexService genreIndexService) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.betaReaderIndexRepository = betaReaderIndexRepository;
        this.genreIndexService = genreIndexService;
    }
}
