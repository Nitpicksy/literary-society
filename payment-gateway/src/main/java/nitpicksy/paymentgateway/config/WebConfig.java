package nitpicksy.paymentgateway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins("https://localhost:3003", "http://localhost:3003", "https://localhost:3000", "http://localhost:3000", "https://localhost:3006", "http://localhost:3006").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}