package nitpicksy.zuulapigateway;

import nitpicksy.zuulapigateway.filters.ZuulPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class ZuulApiGatewayApplication {

    @Bean
    public ZuulPreFilter simpleFilter() {
        return new ZuulPreFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZuulApiGatewayApplication.class, args);
    }

    @Bean
    public ServletWebServerFactory servletContainer() {

        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        // --- CUSTOMIZE SSL PORT IN ORDER TO BE ABLE TO RELOAD THE SSL HOST CONFIG
        tomcat.addConnectorCustomizers(new DefaultSSLConnectorCustomizer());
        return tomcat;
    }
}


