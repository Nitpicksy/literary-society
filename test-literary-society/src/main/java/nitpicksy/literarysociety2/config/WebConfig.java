package nitpicksy.literarysociety2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins("https://localhost:3009", "http://localhost:3009", "https://www.literary-society.com:3009", "http://www.literary-society.com:3009").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}

