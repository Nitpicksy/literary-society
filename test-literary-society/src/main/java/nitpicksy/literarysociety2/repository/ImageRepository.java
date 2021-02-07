package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends RefreshMethodRepository<Image, Long> {

    Image findByData(String data);

}
