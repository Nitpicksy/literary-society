package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends RefreshMethodRepository<Image, Long> {

    Image findByData(String data);

}
