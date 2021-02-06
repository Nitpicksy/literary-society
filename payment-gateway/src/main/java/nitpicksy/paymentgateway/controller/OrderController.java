package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.request.PaymentRequestDTO;
import nitpicksy.paymentgateway.dto.response.OrderDetailsDTO;
import nitpicksy.paymentgateway.mapper.TransactionMapper;
import nitpicksy.paymentgateway.service.OrderService;
import nitpicksy.paymentgateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private TransactionMapper transactionMapper;

    private OrderService orderService;

    /**
     * Creates a new transaction/order from a literary society.
     *
     * @param orderDTO
     * @return redirect URL to GATEWAY_PAYMENT_REDIRECT_URL + / {orderId}
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderRequestDTO orderDTO) {
        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.OK);
    }

    /**
     * @param orderId
     * @return orderDetails based on order id.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable("orderId") @NotNull @Positive(message = "Id must be positive.") Long orderId) {
        return new ResponseEntity<>(transactionMapper.toDto(orderService.findOrder(orderId)), HttpStatus.OK);
    }

    /**
     * POST https://{gateway}/api/orders/forward
     * <p>
     * Endpoint that forwards the order to the payment service
     *
     * @param paymentRequestDTO
     * @return
     */
    @PostMapping("/forward")
    public ResponseEntity<String> forwardPaymentRequest(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        return new ResponseEntity<>(orderService.forwardPaymentRequest(paymentRequestDTO), HttpStatus.OK);
    }

    @Autowired
    public OrderController(TransactionMapper transactionMapper, OrderService orderService) {
        this.transactionMapper = transactionMapper;
        this.orderService = orderService;
    }
}
