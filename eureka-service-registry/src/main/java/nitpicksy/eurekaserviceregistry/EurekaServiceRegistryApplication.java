package nitpicksy.eurekaserviceregistry;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceRegistryApplication.class, args);
    }


//    @Bean
//    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws NoSuchAlgorithmException {
//        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
////        System.setProperty("javax.net.ssl.keyStore", "src/main/resources/service_registry_keyStore.jks");
////        System.setProperty("javax.net.ssl.keyStorePassword", "password");
////        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/service_registry_trustedStore.jks");
////        System.setProperty("javax.net.ssl.trustStorePassword", "password");
//        EurekaJerseyClientImpl.EurekaJerseyClientBuilder builder = new EurekaJerseyClientImpl.EurekaJerseyClientBuilder();
////        builder.withClientName("service-registry");
//        builder.withSystemSSLConfiguration();
//        builder.withMaxTotalConnections(10);
//        builder.withMaxConnectionsPerHost(10);
//        args.setEurekaJerseyClient(builder.build());
//        return args;
//    }

}
