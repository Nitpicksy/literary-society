package nitpicksy.paymentgateway.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.tree.ExpandVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class TrustStoreUtils {
    //change this
    private static String TRUST_STORE_PATH = "payment-gateway/src/main/resources/truststore.p12";
    //change this
    private static String password = "cinema";

    public static KeyStore loadKeyStore() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        char[] keyStorePassArray = password.toCharArray();
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(new FileInputStream(TRUST_STORE_PATH), keyStorePassArray);
        } catch (Exception  e) {
            keyStore.load(null, keyStorePassArray);
        }

        return keyStore;
    }

    public static boolean importCertificateInTrustStore(X509Certificate certificate, String alias, KeyStore truststore){
        try {
            truststore.setCertificateEntry(alias, certificate);
            truststore.containsAlias(alias);
            FileOutputStream fos = new FileOutputStream(TRUST_STORE_PATH);
            truststore.store(fos, password.toCharArray());
            fos.close();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException| IOException  e) {
            return false;
        }
        return true;
    }
}
