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


@FeignClient(name = "plagiator", configuration = FeignClientConfiguration.class, url = "http://localhost:8900/api/")
public interface PlagiatorClient {

    @PostMapping(path = "file/upload/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PaperResultDTO uploadNewBook(@RequestPart MultipartFile file);

    @PostMapping(path = "file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    PaperResultDTO uploadExistingBook(@RequestPart MultipartFile file);

    @GetMapping(path = "file/upload/new/results/{id}")
    PaperResultDTO getPaperResult(@PathVariable Long id);

    @GetMapping(path = "file/download/{id}")
    ResponseEntity<byte[]> downloadPaper(@PathVariable Long id);

    @DeleteMapping(path = "papers/{id}")
    void deletePaper(@PathVariable Long id);

}
