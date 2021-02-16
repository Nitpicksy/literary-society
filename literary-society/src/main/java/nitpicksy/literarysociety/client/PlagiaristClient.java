package nitpicksy.literarysociety.client;

import nitpicksy.literarysociety.config.FeignClientConfiguration;
import nitpicksy.literarysociety.plagiarist.dto.PaperResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(name = "plagiarist", configuration = FeignClientConfiguration.class, url = "http://localhost:8080/")
public interface PlagiaristClient {

    @RequestMapping(method = RequestMethod.POST, path = "api/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PaperResultDTO upload(@RequestPart MultipartFile file);

    @RequestMapping(method = RequestMethod.POST, path = "api/file/upload/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PaperResultDTO uploadNew(@RequestPart MultipartFile file);

    @RequestMapping(method = RequestMethod.GET, path = "api/file/upload/new/results/{id}")
    PaperResultDTO getResult(@PathVariable Long id);
}