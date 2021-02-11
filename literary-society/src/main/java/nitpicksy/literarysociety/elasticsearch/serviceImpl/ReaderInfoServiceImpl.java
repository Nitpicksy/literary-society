package nitpicksy.literarysociety.elasticsearch.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nitpicksy.literarysociety.elasticsearch.model.ReaderInfo;
import nitpicksy.literarysociety.elasticsearch.repository.ReaderInfoRepository;
import nitpicksy.literarysociety.elasticsearch.service.GenreInfoService;
import nitpicksy.literarysociety.elasticsearch.service.ReaderInfoService;
import nitpicksy.literarysociety.model.Genre;
import nitpicksy.literarysociety.model.Reader;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReaderInfoServiceImpl implements ReaderInfoService {

    private ReaderInfoRepository readerInfoRepository;

    private GenreInfoService genreInfoService;

    private GeoPointServiceImpl geoPointService;

    @Override
    public ReaderInfo save(Reader reader) {
        String name = reader.getFirstName() + " " + reader.getLastName();
        ReaderInfo readerInfo = new ReaderInfo(reader.getUserId(), name, reader.getCity(), reader.getCountry());

        List<Long> genres = reader.getBetaReaderGenres().stream().map(Genre::getId).collect(Collectors.toList());
        readerInfo.setGenre(genreInfoService.findByIds(genres));

        GeoPoint geoPoint = geoPointService.getGeoPoint(reader.getCountry() + " " + reader.getCity());

        if(geoPoint != null){
            readerInfo.setGeoPoint(geoPoint);
            return readerInfoRepository.save(readerInfo);
        }
        return null;
    }


    public ReaderInfoServiceImpl(ReaderInfoRepository readerInfoRepository,GenreInfoService genreInfoService,GeoPointServiceImpl geoPointService) {
        this.readerInfoRepository = readerInfoRepository;
        this.genreInfoService = genreInfoService;
        this.geoPointService  = geoPointService;
    }
}
