package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.request.ConfirmPaymentRequestDTO;
import nitpicksy.paymentgateway.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private OrderService orderService;

    @PutMapping(value = "/confirm/{merchantOrderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmPayment(@PathVariable @Positive(message = "Id must be positive.") Long merchantOrderId,
                                               @Valid @RequestBody ConfirmPaymentRequestDTO confirmPaymentRequestDTO) {
        orderService.completeOrder(merchantOrderId, confirmPaymentRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Autowired
    public PaymentController(OrderService orderService) {
        this.orderService = orderService;
    }
}
