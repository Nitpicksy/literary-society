package nitpicksy.paymentgateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@Component
//public class DynamicFeignClient {
//
//    interface MyCall {
//        @RequestMapping(value = "/api/payment/pay", method = RequestMethod.POST)
//        void callService();
//    }
//
//    FeignClientBuilder feignClientBuilder;
//
//    public DynamicFeignClient(@Autowired ApplicationContext appContext) {
//        this.feignClientBuilder = new FeignClientBuilder(appContext);
//    }
//
//    /*
//     * Dynamically call a service registered in the directory.
//     */
//
//    public void doCall(String serviceId) {
//
//        // create a feign client
//
//        MyCall fc =
//                this.feignClientBuilder.forType(MyCall.class, serviceId).build();
//
//        // make the call
//
//        fc.callService();
//    }
//}