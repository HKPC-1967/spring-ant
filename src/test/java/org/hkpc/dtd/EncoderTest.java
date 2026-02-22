package org.hkpc.dtd;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.util.Base64;

@SpringBootTest
@ActiveProfiles("dev")
public class EncoderTest {

    private final PasswordEncoder passwordEncoder ;

    @Autowired
    public EncoderTest(PasswordEncoder  passwordEncoder ) {
        this.passwordEncoder  = passwordEncoder ;
    }

    @Test
    public void encode() {
        String encodedPassword = passwordEncoder.encode("HKPC");

        System.out.println(JSON.toJSONString(encodedPassword));
    }


    @Test
    public void generateJwtKey() {
        // generate a jwt key, use different key for different environment
        SecretKey secretKey = Jwts.SIG.HS512.key().build();
        // encode the key to base64
        String secretKeyStr = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println(JSON.toJSONString(secretKeyStr));
        // then you can encrypt the jwt key then store to the database
    }
}
