package nitpicksy.paymentgateway.utils;

import org.bouncycastle.jcajce.provider.asymmetric.X509;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Component
public class CertificateUtils {

    private static String CERTIFICATES_PATH = "payment-gateway/src/main/resources/certificates/";

    public static String saveCertFile(MultipartFile certificate) throws IOException {
        if (!certificate.isEmpty()) {
            byte[] bytes = certificate.getBytes();
            Path path = Paths.get(CERTIFICATES_PATH + File.separator + certificate.getOriginalFilename());
            if (Files.exists(path)) {
                return null;
            }
            Files.write(path, bytes);
            return certificate.getOriginalFilename();
        }
        return null;
    }

    public static byte[] loadCertFile(String name) throws IOException {
        Path fileStorageLocation = Paths.get(CERTIFICATES_PATH);
        Path filePath = fileStorageLocation.resolve(name).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        File file = resource.getFile();

        return Files.readAllBytes(file.toPath());
    }

    public static X509Certificate  getCertificate(String fileName)
            throws Exception {
        String path = CERTIFICATES_PATH + "/"  + fileName;
        CertificateFactory certificateFactory = CertificateFactory
                .getInstance("X.509");
        FileInputStream in = new FileInputStream(path);

        X509Certificate certificate = (X509Certificate) certificateFactory
                .generateCertificate(in);
        in.close();

        return certificate;
    }

}
