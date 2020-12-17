package nitpicksy.paymentgateway.controller;

import feign.FeignException;
import nitpicksy.paymentgateway.client.ZuulClient;
import nitpicksy.paymentgateway.dto.request.DynamicPaymentDetailsDTO;
import nitpicksy.paymentgateway.dto.request.OrderRequestDTO;
import nitpicksy.paymentgateway.dto.request.PaymentRequestDTO;
import nitpicksy.paymentgateway.dto.response.OrderDetailsDTO;
import nitpicksy.paymentgateway.dto.response.PaymentResponseDTO;
import nitpicksy.paymentgateway.mapper.TransactionMapper;
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
import java.net.URI;

@Validated
@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {


    @Value("${API_GATEWAY_URL}")
    private String apiGatewayURL;

    private TransactionMapper transactionMapper;

    private OrderService orderService;

    private ZuulClient zuulClient;


    /**
     * Creates a new transaction/order from a literary society.
     *
     * @param orderDTO
     * @return redirect URL to GATEWAY_PAYMENT_REDIRECT_URL + / {orderId}
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@Valid @RequestBody OrderRequestDTO orderDTO) {
        String redirectURL = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(redirectURL, HttpStatus.OK);
    }

    /**
     * @param orderId
     * @return orderDetails based on order id.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable("orderId") @Positive(message = "Id must be positive.") Long orderId) {
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
    public ResponseEntity<String> forwardPaymentRequest(@RequestBody PaymentRequestDTO paymentRequestDTO) {

        DynamicPaymentDetailsDTO forwardDTO = orderService.forwardPaymentRequest(paymentRequestDTO.getOrderId(), paymentRequestDTO.getPaymentCommonName());
        String responseURL;
        try {
            PaymentResponseDTO dto = zuulClient.forwardPaymentRequest(URI.create(apiGatewayURL + '/' + paymentRequestDTO.getPaymentCommonName()), forwardDTO);
            orderService.setPayment(paymentRequestDTO.getOrderId(), dto.getPaymentId());
            responseURL = dto.getPaymentURL();
        } catch (FeignException.FeignClientException e) {
            e.printStackTrace();
            //if bank request fails, redirect user to the company failedURL;
            orderService.cancelOrder(paymentRequestDTO.getOrderId());
            responseURL = forwardDTO.getFailedURL();
        }
        return new ResponseEntity<>(responseURL, HttpStatus.OK);
    }

    @Autowired
    public OrderController(OrderService orderService, TransactionMapper transactionMapper, ZuulClient zuulClient) {
        this.orderService = orderService;
        this.transactionMapper = transactionMapper;
        this.zuulClient = zuulClient;
    }

}
