package nitpicksy.paypalservice.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import nitpicksy.paypalservice.client.ZuulClient;
import nitpicksy.paypalservice.dto.request.SubscriptionPlanDTO;
import nitpicksy.paypalservice.dto.response.ConfirmPaymentResponseDTO;
import nitpicksy.paypalservice.exceptionHandler.InvalidDataException;
import nitpicksy.paypalservice.model.Log;
import nitpicksy.paypalservice.service.LogService;
import nitpicksy.paypalservice.service.SubscriptionService;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final String MODE = "sandbox";
    private static final String CONFIRMATION_URL = "https://localhost:8200/api/payments/confirm";

    //API Source: https://exchangerate.host/
    private static final String CURRENCY_CONVERSION_API = "https://api.exchangerate.host/convert";
    private static final Double BACKUP_CONVERSION_RATE = 0.01034;

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private LogService logService;

    @Override
    public String createBillingPlan(SubscriptionPlanDTO dto) {
        String productId = createProduct(dto);

        JSONObject freqObj = new JSONObject();
        freqObj.put("interval_unit", dto.getFrequencyUnit());
        freqObj.put("interval_count", dto.getFrequencyCount());

        JSONObject priceObj = new JSONObject();
        priceObj.put("value", convertCurrency(dto.getPrice()));
        priceObj.put("currency_code", "USD");

        JSONObject cyclesObj = new JSONObject();
        cyclesObj.put("frequency", freqObj);
        cyclesObj.put("tenure_type", "REGULAR");
        cyclesObj.put("sequence", 1);
        cyclesObj.put("total_cycles", 0);
        cyclesObj.put("pricing_scheme", new JSONObject().put("fixed_price", priceObj));

        JSONArray cyclesArr = new JSONArray();
        cyclesArr.put(cyclesObj);

        JSONObject paymentsObj = new JSONObject();
        paymentsObj.put("auto_bill_outstanding", "true");
        paymentsObj.put("setup_fee_failure_action", "CONTINUE");
        paymentsObj.put("payment_failure_threshold", 1);

        JSONObject planObj = new JSONObject();
        planObj.put("product_id", productId);
        planObj.put("name", dto.getPlanName());
        planObj.put("description", dto.getPlanDescription());
        planObj.put("status", "ACTIVE");
        planObj.put("billing_cycles", cyclesArr);
        planObj.put("payment_preferences", paymentsObj);

        String billingPlansAPI = "https://api.sandbox.paypal.com/v1/billing/plans";
        JSONObject responseJSON = sendRequest(billingPlansAPI, planObj,
                dto.getMerchantClientId(), dto.getMerchantClientSecret());

        String planId = responseJSON.getString("id");

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPR",
                String.format("PayPal billing plan with planId=%s successfully created.", planId)));

        return planId;
    }

    @Override
    public String createSubscription(String planId) {
        return null;
    }

    private String createProduct(SubscriptionPlanDTO dto) {
        JSONObject productObj = new JSONObject();
        productObj.put("name", dto.getProductName());
        productObj.put("type", dto.getProductType());
        productObj.put("category", dto.getProductCategory());

        String productsAPI = "https://api.sandbox.paypal.com/v1/catalogs/products";
        JSONObject responseJSON = sendRequest(productsAPI, productObj,
                dto.getMerchantClientId(), dto.getMerchantClientSecret());

        String productId = responseJSON.getString("id");

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPR",
                String.format("PayPal product with productId=%s successfully created.", productId)));

        return productId;
    }

    private JSONObject sendRequest(String url, JSONObject body, String clientId, String clientSecret) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders() {{
            String auth = clientId + ":" + clientSecret;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            setContentType(MediaType.APPLICATION_JSON);
        }};
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (RestClientException e) {
            String thing = "";
            String eventCode = "";
            if (url.endsWith("/products")) {
                thing = "product";
                eventCode = "CPR";
            } else if (url.endsWith("/plans")) {
                thing = "billing plan";
                eventCode = "CBP";
            }
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, eventCode,
                    String.format("PayPal %s '%s' creation failed. Invalid request sent.", thing, body.getString("name"))));
            throw new InvalidDataException("PayPal subscription plan could not be created. Please try again later.", HttpStatus.BAD_REQUEST);
        }

        return new JSONObject(response.getBody());
    }

    private String convertCurrency(Double baseAmount) {
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
    public SubscriptionServiceImpl(LogService logService) {
        this.logService = logService;
    }
}
