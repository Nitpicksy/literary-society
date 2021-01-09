package nitpicksy.paypalservice.controller;

import nitpicksy.paypalservice.dto.request.SubscriptionDTO;
import nitpicksy.paypalservice.dto.request.SubscriptionPlanDTO;
import nitpicksy.paypalservice.mapper.SubscriptionPlanMapper;
import nitpicksy.paypalservice.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Validated
@RestController
@RequestMapping(value = "/api/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    private SubscriptionPlanMapper subscriptionPlanMapper;

    @PostMapping(value = "/create-plan", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) {
        return new ResponseEntity<>(subscriptionService.createBillingPlan(
                subscriptionPlanMapper.toEntity(subscriptionPlanDTO)), HttpStatus.OK);
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<String> subscribe(@Valid @RequestBody SubscriptionDTO subscriptionDTO) {
        return new ResponseEntity<>(subscriptionService.subscribe(subscriptionDTO), HttpStatus.OK);
    }

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, SubscriptionPlanMapper subscriptionPlanMapper) {
        this.subscriptionService = subscriptionService;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
    }
}
