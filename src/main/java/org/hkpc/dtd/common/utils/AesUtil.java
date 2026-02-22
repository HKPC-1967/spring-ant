package org.hkpc.dtd.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.SecureRandom;

/*****************************************************
 * AES encryption and decryption utility
 ****************************************************/

@Slf4j
public class AesUtil {
    // Encoding
    private static final String ENCODING = "UTF-8";
    // Algorithm definition
    private static final String AES_ALGORITHM = "AES";
    // Specify padding mode
    private static final String CIPHER_MODEL_PADDING = "AES/ECB/PKCS5Padding";
    private static final String RANDOM_CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();


    /**
     * AES_ECB encryption
     *
     * @param content Content to be encrypted
     * @return Encrypted content
     */
    public static String encrypt(String content, String aesKey) {
        if (StringUtils.isBlank(content)) {
            log.info("AES_ECB encrypt: the content is null!");
            return null;
        }
        // Check if the key is 16 characters long
        if (StringUtils.isNotBlank(aesKey) && aesKey.length() == 16) {
            try {
                // Encode the key
                byte[] bytes = aesKey.getBytes(ENCODING);
                // Set encryption algorithm and generate key
                SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);
                // "Algorithm/Mode/Padding"
                Cipher cipher = Cipher.getInstance(CIPHER_MODEL_PADDING);
                // Select encryption mode
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                // Generate byte array from content to be encrypted
                byte[] encrypted = cipher.doFinal(content.getBytes(ENCODING));
                // Return base64 encoded string
                return Base64.getEncoder().encodeToString(encrypted);
            } catch (Exception e) {
                log.error("AES encrypt exception:{}", e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            log.info("AES encrypt: the aesKey is null or error!");
            return null;
        }
    }

    /**
     * AES_ECB decryption
     *
     * @param content Content to be decrypted
     * @return Decrypted content
     */
    public static String decrypt(String content, String aesKey) {
        if (StringUtils.isBlank(content)) {
            log.info("AES_ECB decrypt: the content is null!");
            return null;
        }
        // Check if the key is 16 characters long
        if (StringUtils.isNotBlank(aesKey) && aesKey.length() == 16) {
            try {
                // Encode the key
                byte[] bytes = aesKey.getBytes(ENCODING);
                // Set decryption algorithm and generate key
                SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, AES_ALGORITHM);

                // "Algorithm/Mode/Padding"
                Cipher cipher = Cipher.getInstance(CIPHER_MODEL_PADDING);
                // Select decryption mode
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

                // First decode base64
                byte[] decodeBase64 = Base64.getDecoder().decode(content);

                // Decrypt the content
                byte[] decrypted = cipher.doFinal(decodeBase64);
                // Convert byte array to string
                return new String(decrypted, ENCODING);
            } catch (Exception e) {
                log.error("AES_ECB decrypt exception:{}", e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            log.info("AES_ECB decrypt: the aesKey is null or error!");
            return null;
        }
    }

    /**
    * you can run this main method to generate the encrypted content for your sensitive data, then save the encrypted content to DB, and use the same aesKey to decrypt it when you get it from DB.<br>
     * Note: the aesKey must be 16 characters long, and you should keep the aesKey safe and do not hard code it in your code.
     *
    */
    public static void main(String[] args) {
        System.out.println("================== generate new aesKey and encrypt jwtKey =================");
        generateNewAesKeyAndEncryptedJwtKey();

//        System.out.println("================== AES Encryption and Decryption TEST =================");
//        testAesEncryptionAndDecrytion();
    }

    /**
     * Generate a new AES key and JWT key, then encrypt the JWT key for DB initialization.
     */
    private static void generateNewAesKeyAndEncryptedJwtKey() {
        // 1) Generate a random AES key, must be 16 characters long.
        String newAesKey = generateRandomString(16);
        System.out.println("new aes_key (16 chars):");
        System.out.println(newAesKey);
        System.out.println();

        // 2) Generate a random JWT key (base64 encoded random bytes).
        String newJwtKey = Base64.getEncoder().encodeToString(generateRandomBytes(64));
//        System.out.println("new jwtKey:");
//        System.out.println(newJwtKey);

        // 3) Encrypt JWT key with AES key and print the encrypted value for DB.
        String encryptedJwtKey = encrypt(newJwtKey, newAesKey);
        System.out.println("encrypted jwt_key (save this to db_config.value where key='jwt_key'):");
        System.out.println(encryptedJwtKey);
        System.out.println();
        
        String updateSql = "UPDATE db_config\n"
            + "SET value = '" + encryptedJwtKey + "',\n"
            + "updated_at = CURRENT_TIMESTAMP\n"
            + "WHERE key = 'jwt_key';";
        System.out.println("SQL to update db_config.jwt_key:");
        System.out.println(updateSql);
    }

    private static byte[] generateRandomBytes(int byteLength) {
        byte[] bytes = new byte[byteLength];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

    private static String generateRandomString(int length) {
        StringBuilder keyBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(RANDOM_CHAR_POOL.length());
            keyBuilder.append(RANDOM_CHAR_POOL.charAt(randomIndex));
        }
        return keyBuilder.toString();
    }

    /**
     * Test AES encryption and decryption with a sample content. You can run this method to verify that the encryption and decryption work correctly with the same AES key.
     */
    private static void testAesEncryptionAndDecryption() {
        // Encryption key must be 16 characters long
        final String aesKey = "osGvEaD8qoEKLUXT";
        // JWT website (secret base64 encoded) https://jwt.io/
        // Encryption key must be 16 characters long
        String content = "hello world";
        String contentEncrypt = encrypt(content,aesKey);
        String deContent = decrypt(contentEncrypt,aesKey);
        System.out.println("encrypted content:");
        System.out.println(contentEncrypt);
        System.out.println("decrypted content:");
        System.out.println(deContent);
        System.out.println(content.equals(decrypt(contentEncrypt,aesKey)));
    }
}
