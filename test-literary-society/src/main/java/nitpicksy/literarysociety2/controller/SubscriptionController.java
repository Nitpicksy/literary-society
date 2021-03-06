package nitpicksy.literarysociety2.controller;

import nitpicksy.literarysociety2.dto.request.SubscriptionPlanDTO;
import nitpicksy.literarysociety2.dto.response.SubscriptionPlanResponseDTO;
import nitpicksy.literarysociety2.model.PriceList;
import nitpicksy.literarysociety2.service.PriceListService;
import nitpicksy.literarysociety2.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/api/subscriptions")
public class SubscriptionController {

    private SubscriptionService subscriptionService;

    private PriceListService priceListService;

    @GetMapping(value = "/plan")
    public ResponseEntity<SubscriptionPlanResponseDTO> getSubscriptionPlans(@NotBlank @RequestParam(name = "for") String nameSubstring) {
        return new ResponseEntity<>(subscriptionService.getSubscriptionPlan(nameSubstring), HttpStatus.OK);
    }

    @PostMapping(value = "/subscribe")
    public ResponseEntity<String> subscribe(@NotNull @Positive @RequestParam Long planId) {
        return new ResponseEntity<>(subscriptionService.subscribe(planId), HttpStatus.OK);
    }

    @PostMapping(value = "/create-membership")
    public ResponseEntity<Void> createMembership(@NotBlank @RequestParam String subscriptionId) {
        subscriptionService.createMembership(subscriptionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@NotBlank @RequestParam Long planId) {
        subscriptionService.unsubscribe(planId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/create-plans")
    public ResponseEntity<Void> createSubscriptionPlans() {
        PriceList currentPriceList = priceListService.findLatestPriceList();

        SubscriptionPlanDTO writerMembership = new SubscriptionPlanDTO(null, null, "Book Publishing Option", "DIGITAL",
                "BOOKS_MANUSCRIPTS", "Writer Membership", "Create publication requests and publish books via Literary Society.",
                currentPriceList.getMembershipForWriter(), "MONTH", 3, "https://www.literary-society.com:3000/subscription/success",
                "https://www.literary-society.com:3000");

        subscriptionService.createSubscriptionPlan(writerMembership);

        SubscriptionPlanDTO readerMembership = new SubscriptionPlanDTO(null, null, "Book Purchasing Discount", "DIGITAL",
                "BOOKS_MANUSCRIPTS", "Reader Membership", "Buy books with discount from all Literary Society merchants.",
                currentPriceList.getMembershipForReader(), "MONTH", 1, "https://www.literary-society.com:3000/subscription/success",
                "https://www.literary-society.com:3000");

        subscriptionService.createSubscriptionPlan(readerMembership);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, PriceListService priceListService) {
        this.subscriptionService = subscriptionService;
        this.priceListService = priceListService;
    }
}
