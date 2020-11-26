package nitpicksy.literarysociety;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LiterarySocietyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiterarySocietyApplication.class, args);
	}

}
