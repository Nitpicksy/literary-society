package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.request.SubscriptionPlanDTO;
import nitpicksy.paymentgateway.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(value = "/api/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    @PostMapping(value = "/create-plan")
    public ResponseEntity<String> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) {
        return new ResponseEntity<>(subscriptionService.createSubscriptionPlan(subscriptionPlanDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<String> subscribe(@Valid @RequestParam String type) {
//        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.OK);
        return null;
    }

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
