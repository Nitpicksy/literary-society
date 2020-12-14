package nitpicksy.paymentgateway.controller;

import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.response.OrderDetailsDTO;
import nitpicksy.paymentgateway.dto.response.OrderResponseDTO;
import nitpicksy.paymentgateway.dto.response.PaymentMethodResponseDTO;
import nitpicksy.paymentgateway.exceptionHandler.InvalidDataException;
import nitpicksy.paymentgateway.mapper.PaymentMethodMapper;
import nitpicksy.paymentgateway.mapper.TransactionMapper;
import nitpicksy.paymentgateway.model.Transaction;
import nitpicksy.paymentgateway.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Validated
@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Value("${GATEWAY_PAYMENT_REDIRECT_URL}")
    private String gatewayRedirectUrl;

    private TransactionMapper transactionMapper;
    
    private OrderService orderService;

    /**
     * Creates a new transaction/order from a literary society.
     *
     * @param orderDTO
     * @return redirect URL to GATEWAY_PAYMENT_REDIRECT_URL + / {orderId}
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderDTO) {
        // create transaction
        Transaction order = orderService.createOrder(orderDTO);

        if (order == null) {
            throw new InvalidDataException("Error while creating new order.", HttpStatus.BAD_REQUEST);
        }
        //forward to payment methods url
        return new ResponseEntity<>(new OrderResponseDTO(gatewayRedirectUrl + "/" + order.getId()), HttpStatus.OK);
    }

    /**
     * @param orderId
     * @return orderDetails based on order id.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable("orderId") @Positive(message = "Id must be positive.") Long orderId) {
        return new ResponseEntity<>(transactionMapper.toDto(orderService.findOrder(orderId)), HttpStatus.OK);
    }

    @Autowired
    public OrderController(OrderService orderService, TransactionMapper transactionMapper) {
        this.orderService = orderService;
        this.transactionMapper = transactionMapper;
    }

}
