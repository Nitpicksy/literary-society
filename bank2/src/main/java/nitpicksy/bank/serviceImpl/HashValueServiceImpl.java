package nitpicksy.bank.serviceImpl;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashValueServiceImpl {

    public String getHashValue(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        digest.update(token.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    public String bcryptHash(String value){
        return BCrypt.hashpw(value, BCrypt.gensalt(12));
    }
    //
//        String  originalPassword = "password1";
//        String generatedSecuredPasswordHash = BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
//        System.out.println(generatedSecuredPasswordHash);
//        String haj = "$2a$12$sHyGfonUFrIAtnHiWGxCYuh6nu0GXTCmvFV.KnOxtlZXbzX2x.FiC";
//        if(haj.equals(generatedSecuredPasswordHash)){
//            System.out.println("vavava");
//        }
//        boolean matched1 = BCrypt.checkpw(originalPassword, "$2a$12$sHyGfonUFrIAtnHiWGxCYuh6nu0GXTCmvFV.KnOxtlZXbzX2x.FiC");
//        System.out.println(matched1);
//        boolean matched = BCrypt.checkpw(originalPassword, generatedSecuredPasswordHash);
//        System.out.println(matched);
}
