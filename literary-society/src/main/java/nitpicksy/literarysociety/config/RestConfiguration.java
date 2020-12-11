//package nitpicksy.literarysociety.config;
//
//import org.apache.http.client.HttpClient;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContexts;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.SSLContext;
//import java.io.File;
//import java.io.FileInputStream;
//import java.security.KeyStore;
//
//@Configuration
//public class RestConfiguration {
//
//    private static final String URL_FORMAT = "%s://%s:%s";
//
//    /**
//     * TLS version.
//     */
//    @Value("${server.ssl.algorithm:TLSv1.3}")
//    private String algorithm;
//
//    /**
//     * Application keystore path.
//     */
//    @Value("${server.ssl.key-store:literary.keystore.p12}")
//    private String keystore;
//
//    /**
//     * Application keystore type.
//     */
//    @Value("${server.ssl.key-store-type}")
//    private String keystoreType;
//
//    /**
//     * Application keystore password.
//     */
//    @Value("${server.ssl.key-store-password}")
//    private String keystorePassword;
//
//    /**
//     * Keystore alias for application client credential.
//     */
//    @Value("${server.ssl.key-alias}")
//    private String applicationKeyAlias;
//
//    /**
//     * Application truststore path.
//     */
//    @Value("${server.ssl.trust-store:literary.truststore.p12}")
//    private String truststore;
//
//    /**
//     * Application truststore type.
//     */
//    @Value("${server.ssl.trust-store-type}")
//    private String truststoreType;
//
//    /**
//     * Application truststore password.
//     */
//    @Value("${server.ssl.trust-store-password}")
//    private String truststorePassword;
//
//    @Value("${PROTOCOL:https}")
//    private String protocol;
//
//    @Value("${DOMAIN:localhost}")
//    private String domain;
//
//    @Value("${PORT:8090}")
//    private String port;
//
//    public String url() {
//        return String.format(URL_FORMAT, protocol, domain, port);
//    }
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(
//                httpClient(keystoreType, keystore, keystorePassword, applicationKeyAlias,
//                        truststoreType, truststore, truststorePassword)));
//    }
//
//
//    @Bean
//    public HttpClient httpClient(String keystoreType, String keystore, String keystorePassword, String alias,
//                                 String truststoreType, String truststore, String truststorePassword) {
//        try {
//            KeyStore keyStore = KeyStore.getInstance(keystoreType);
//            keyStore.load(new FileInputStream(new File(keystore)), keystorePassword.toCharArray());
//
//            KeyStore trustStore = KeyStore.getInstance(truststoreType);
//            trustStore.load(new FileInputStream(new File(truststore)), truststorePassword.toCharArray());
//
//            SSLContext sslcontext = SSLContexts.custom()
//                    .loadTrustMaterial(trustStore, null)
//                    .loadKeyMaterial(keyStore, keystorePassword.toCharArray(), (aliases, socket) -> alias)
//                    .build();
//
//            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslcontext,
//                    new String[]{algorithm},
//                    null, (hostname, sslSession) -> true);
//
//            return HttpClients.custom().setSSLSocketFactory(sslFactory).build();
//        } catch (Exception e) {
//            throw new IllegalStateException("Error while configuring rest template", e);
//        }
//    }
//}