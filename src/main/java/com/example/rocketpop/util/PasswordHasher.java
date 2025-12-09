package com.example.rocketpop.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
public class PasswordHasher {

    private static final Logger logger = LoggerFactory.getLogger(PasswordHasher.class);

    @Value("${private.key}")
    private String privateKeyString;


    /**
     * Decrypt username and password sent by user, then generate and return a salted hash of the password.
     */
    public String hashPassword(String password, String salt) {
        // Hash password
        byte[] hashedPassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            md.update(saltBytes);
            hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("Error hashing password", e);
        }

        return hashedPassword.toString();
    }

    public String getRandomSalt() {
        SecureRandom rng = new SecureRandom();
        byte[] salt = new byte[16];

        try {
            rng.nextBytes(salt);
        } catch (NullPointerException e) {
            logger.error("Error generating random salt", e);
        }
        return Base64.getEncoder().encodeToString(salt);
    }

    public String rsaDecrypt(String ciphertext) {
        Base64.Decoder decoder = Base64.getDecoder();
        String plaintext = null;
        PrivateKey privateKey = null;

        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] privateKeyByte = decoder.decode(privateKeyString);
            privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
        } catch (Exception e) {
            logger.error("Error decrypting password", e);
        }

        // Decrypt ciphertext
        try {
            byte[] cipherBytes = decoder.decode(ciphertext);
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptBytes = decryptCipher.doFinal(cipherBytes);
            plaintext = new String(decryptBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error decrypting password", e);
        }

        return plaintext;
    }


}
