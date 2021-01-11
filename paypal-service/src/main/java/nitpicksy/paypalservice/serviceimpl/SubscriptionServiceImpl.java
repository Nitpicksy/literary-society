package nitpicksy.paypalservice.serviceimpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nitpicksy.paypalservice.dto.request.CancelSubscriptionDTO;
import nitpicksy.paypalservice.dto.request.SubscriptionDTO;
import nitpicksy.paypalservice.dto.request.SubscriptionPlanDTO;
import nitpicksy.paypalservice.exceptionHandler.InvalidDataException;
import nitpicksy.paypalservice.model.Log;
import nitpicksy.paypalservice.model.SubscriptionPlan;
import nitpicksy.paypalservice.repository.SubscriptionPlanRepository;
import nitpicksy.paypalservice.service.LogService;
import nitpicksy.paypalservice.service.SubscriptionService;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    //API Source: https://exchangerate.host/
    private static final String CURRENCY_CONVERSION_API = "https://api.exchangerate.host/convert";
    private static final Double BACKUP_CONVERSION_RATE = 0.01034;

    private final String CLASS_PATH = this.getClass().getCanonicalName();
    private final String CLASS_NAME = this.getClass().getSimpleName();

    private SubscriptionPlanRepository subscriptionPlanRepository;

    private LogService logService;

    @Override
    public String createBillingPlan(SubscriptionPlan plan) {
        String productId = createProduct(plan);

        JSONObject freqObj = new JSONObject();
        freqObj.put("interval_unit", plan.getFrequencyUnit());
        freqObj.put("interval_count", String.valueOf(plan.getFrequencyCount()));

        JSONObject priceObj = new JSONObject();
        priceObj.put("value", convertCurrency(plan.getPrice()));
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
        planObj.put("name", plan.getPlanName());
        planObj.put("description", plan.getPlanDescription());
        planObj.put("status", "ACTIVE");
        planObj.put("billing_cycles", cyclesArr);
        planObj.put("payment_preferences", paymentsObj);

        String billingPlansAPI = "https://api.sandbox.paypal.com/v1/billing/plans";
        JSONObject responseJSON = sendRequest(billingPlansAPI, planObj,
                plan.getMerchantClientId(), plan.getMerchantClientSecret());

        String planId = responseJSON.getString("id");
        plan.setPlanId(planId);
        subscriptionPlanRepository.save(plan);

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPR",
                String.format("Billing plan with planId=%s successfully created.", planId)));

        return planId;
    }

    @Override
    public String subscribe(SubscriptionDTO dto) {
        SubscriptionPlan plan = subscriptionPlanRepository.findOneByPlanId(dto.getPlanId());
        if (plan == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                    "Subscription creation failed. Invalid plan id sent."));
            throw new InvalidDataException("Subscription could not be created. Invalid plan id provided.", HttpStatus.BAD_REQUEST);
        }

        JSONObject contextObj = new JSONObject();
        contextObj.put("shipping_preference", "NO_SHIPPING");
        contextObj.put("user_action", "SUBSCRIBE_NOW");
        contextObj.put("return_url", plan.getSuccessURL());
        contextObj.put("cancel_url", plan.getCancelURL());

        JSONObject subscriptionObj = new JSONObject();
        subscriptionObj.put("plan_id", plan.getPlanId());
        subscriptionObj.put("quantity", "1");
        subscriptionObj.put("application_context", contextObj);
        if (dto.getStartDate() != null) {
            LocalDateTime startDateTime = LocalDateTime.of(dto.getStartDate(), LocalTime.MIDNIGHT);
            subscriptionObj.put("start_time", startDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z");
        }

        String subscriptionsAPI = "https://api.sandbox.paypal.com/v1/billing/subscriptions";
        JSONObject responseJSON = sendRequest(subscriptionsAPI, subscriptionObj,
                plan.getMerchantClientId(), plan.getMerchantClientSecret());

        String redirectUrl = responseJSON.getJSONArray("links").getJSONObject(0).getString("href");

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                String.format("Subscription for plan with planId=%s successfully created.", plan.getPlanId())));

        return redirectUrl;
    }

    @Override
    public void unsubscribe(CancelSubscriptionDTO dto) {
        SubscriptionPlan plan = subscriptionPlanRepository.findOneByPlanId(dto.getPlanId());
        if (plan == null) {
            logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                    "PayPal subscription canceling failed. Invalid plan id sent."));
            throw new InvalidDataException("PayPal subscription could not be canceled. Invalid plan id provided.", HttpStatus.BAD_REQUEST);
        }

        JSONObject payloadObj = new JSONObject();
        payloadObj.put("reason", "Don't need subscription benefits anymore.");

        String subscriptionsAPI = "https://api.sandbox.paypal.com/v1/billing/subscriptions/" + dto.getSubscriptionId() + "/cancel";
        sendRequest(subscriptionsAPI, payloadObj, plan.getMerchantClientId(), plan.getMerchantClientSecret());

        logService.write(new Log(Log.INFO, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                String.format("Subscription for plan with planId=%s successfully canceled.", plan.getPlanId())));
    }

    private String createProduct(SubscriptionPlan plan) {
        JSONObject productObj = new JSONObject();
        productObj.put("name", plan.getProductName());
        productObj.put("type", plan.getProductType());
        productObj.put("category", plan.getProductCategory());

        String productsAPI = "https://api.sandbox.paypal.com/v1/catalogs/products";
        JSONObject responseJSON = sendRequest(productsAPI, productObj,
                plan.getMerchantClientId(), plan.getMerchantClientSecret());

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
            if (url.endsWith("/products")) {
                logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CPR",
                        String.format("Product '%s' creation failed. Invalid request sent.", body.getString("name"))));
                throw new InvalidDataException("Subscription plan could not be created. Please try again later.", HttpStatus.BAD_REQUEST);
            } else if (url.endsWith("/plans")) {
                logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "CBP",
                        String.format("Billing plan '%s' creation failed. Invalid request sent.", body.getString("name"))));
                throw new InvalidDataException("Subscription plan could not be created. Please try again later.", HttpStatus.BAD_REQUEST);
            } else if (url.endsWith("/subscriptions")) {
                logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "SUB",
                        "Subscription creation failed. Invalid request sent."));
                throw new InvalidDataException("Subscription could not be created. Please try again later.", HttpStatus.BAD_REQUEST);
            } else if (url.endsWith("/cancel")) {
                logService.write(new Log(Log.ERROR, Log.getServiceName(CLASS_PATH), CLASS_NAME, "UNS",
                        "Subscription cancellation failed. Invalid request sent."));
                throw new InvalidDataException("Subscription could not be canceled. Please try again later.", HttpStatus.BAD_REQUEST);
            } else {
                throw new InvalidDataException("Something went wrong. Please try again later.", HttpStatus.BAD_REQUEST);
            }
        }

        return response.getBody() != null ? new JSONObject(response.getBody()) : null;
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
    public SubscriptionServiceImpl(SubscriptionPlanRepository subscriptionPlanRepository, LogService logService) {
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.logService = logService;
    }
}
