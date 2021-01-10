package nitpicksy.literarysociety.config;

import feign.Client;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.cert.X509Certificate;

@Configuration
public class FeignClientConfiguration {

    private static String KEY_STORE_PATH = "literary-society/src/main/resources/literary.keystore.p12";

    private static String TRUST_STORE_PATH = "literary-society/src/main/resources/literary.truststore.p12";

    private static String password = "password";

    @Bean
    public Client feignClient() {
        Client trustSSLSockets = new Client.Default(getSSLSocketFactory(), new NoopHostnameVerifier());
        return trustSSLSockets;
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType){
                    return true;
                }
            };
            SSLContext sslContext = SSLContextBuilder
                    .create()
                    .loadKeyMaterial(ResourceUtils.getFile(KEY_STORE_PATH), password.toCharArray(), password.toCharArray())
                    .loadTrustMaterial(ResourceUtils.getFile(TRUST_STORE_PATH), password.toCharArray())
                    .build();
            return sslContext.getSocketFactory();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
