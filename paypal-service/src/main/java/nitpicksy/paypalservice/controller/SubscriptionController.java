package nitpicksy.paypalservice.controller;

import nitpicksy.paypalservice.dto.request.SubscriptionPlanDTO;
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

    @PostMapping(value = "/create-plan", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) {
        return new ResponseEntity<>(subscriptionService.createBillingPlan(subscriptionPlanDTO), HttpStatus.OK);
    }

    @GetMapping(value = "/subscribe")
    public ResponseEntity<Void> subscribe(@RequestParam String planId) {
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(subscriptionService.createSubscription(planId)))
                .build();
    }

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

}
