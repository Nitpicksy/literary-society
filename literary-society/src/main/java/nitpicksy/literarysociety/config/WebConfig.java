package nitpicksy.literarysociety.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins("https://localhost:3000", "http://localhost:3000", "https://www.literary-society.com:3000", "http://www.literary-society.com:3000",
                        "https://192.168.43.199:3000", "http://192.168.43.199:3000").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}

