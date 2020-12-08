package nitpicksy.cloudconfig;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableConfigServer
public class CloudConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigApplication.class, args);
    }
//    @Bean
//    public DiscoveryClient.DiscoveryClientOptionalArgs discoveryClientOptionalArgs() throws NoSuchAlgorithmException {
//        DiscoveryClient.DiscoveryClientOptionalArgs args = new DiscoveryClient.DiscoveryClientOptionalArgs();
//        File keystore = new File("src/main/resources/cloud.keystore.p12");
//        File trustStore = new File("src/main/resources/cloud.truststore.p12");
//        System.out.println("Keystore " + keystore.getAbsolutePath() + " " + keystore.getName());
//        System.setProperty("javax.net.ssl.keyStore", keystore.getAbsolutePath());
//        System.setProperty("javax.net.ssl.keyStorePassword", "password");
//        System.setProperty("javax.net.ssl.keyStoreType", "password");
//        System.setProperty("javax.net.ssl.trustStore", trustStore.getAbsolutePath());
////        System.setProperty("javax.net.ssl.trustStorePassword", "password");
//        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
//        EurekaJerseyClientImpl.EurekaJerseyClientBuilder builder = new EurekaJerseyClientImpl.EurekaJerseyClientBuilder();
//        builder.withClientName("cloud-config");
//        builder.withSystemSSLConfiguration();
//        builder.withMaxTotalConnections(10);
//        builder.withMaxConnectionsPerHost(10);
//        args.setEurekaJerseyClient(builder.build());
//        return args;
//    }

}
