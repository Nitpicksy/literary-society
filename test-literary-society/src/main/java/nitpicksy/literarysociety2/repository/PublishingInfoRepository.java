package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.PublishingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishingInfoRepository extends JpaRepository<PublishingInfo, Long> {
}
