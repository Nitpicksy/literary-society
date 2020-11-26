package nitpicksy.zuulapigateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class URLConfig {

    @Value("${test.value}")
    private String testValue;

    public String getTestValue() {
        return testValue;
    }

}
