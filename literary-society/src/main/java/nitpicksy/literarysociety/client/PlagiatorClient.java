package nitpicksy.literarysociety.client;

import nitpicksy.literarysociety.config.FeignClientConfiguration;
import nitpicksy.literarysociety.dto.request.*;
import nitpicksy.literarysociety.dto.response.MerchantPaymentGatewayResponseDTO;
import nitpicksy.literarysociety.elastic.dto.PaperResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@FeignClient(name = "plagiator", configuration = FeignClientConfiguration.class, url = "http://localhost:8900/")
public interface PlagiatorClient {

    @RequestMapping(method = RequestMethod.POST, path = "api/file/upload/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PaperResultDTO uploadNewBook(@RequestPart MultipartFile file);

    @RequestMapping(method = RequestMethod.POST, path = "api/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PaperResultDTO uploadExistingBook(@RequestPart MultipartFile file);

}
