package nitpicksy.qrservice.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nitpicksy.qrservice.model.Log;
import nitpicksy.qrservice.service.CurrencyService;
import nitpicksy.qrservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private RestTemplate restTemplate;
    private LogService logService;

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private static final String CURRENCY_CONVERSION_API = "https://api.exchangerate.host/convert";
    private static final Double BACKUP_CONVERSION_RATE = 0.01034;

    @Override
    public String convertCurrency(Double baseAmount) {
        BigDecimal bd1 = new BigDecimal(baseAmount * BACKUP_CONVERSION_RATE);
        String convertedAmount = bd1.setScale(2, RoundingMode.HALF_UP).toPlainString();

        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(CURRENCY_CONVERSION_API)
                .queryParam("from", "RSD")
                .queryParam("to", "USD")
                .queryParam("amount", baseAmount);

        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
        } catch (RestClientException e) {
            response = null;
        }

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String result = root.path("result").asText();
                if (!result.isBlank()) {
                    BigDecimal bd2 = new BigDecimal(result);
                    convertedAmount = bd2.setScale(2, RoundingMode.HALF_UP).toPlainString();
                } else {
                    logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONV",
                            "Currency conversion API response changed. Backup conversion rate applied."));
                }
            } catch (JsonProcessingException e) {
                logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONV",
                        "Currency conversion API returned invalid JSON response. Backup conversion rate applied."));
            }
        } else {
            logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CONV",
                    "Currency conversion API response status is not 200 OK. Backup conversion rate applied."));
        }

        return convertedAmount;
    }

    @Autowired
    public CurrencyServiceImpl(RestTemplate restTemplate, LogService logService) {
        this.restTemplate = restTemplate;
        this.logService = logService;
    }
}
