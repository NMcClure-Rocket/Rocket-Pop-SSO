package com.example.rocketpop.util;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class PasswordHasher {

    private static final Logger logger = LoggerFactory.getLogger(PasswordHasher.class);

    @Value("${private.key}")
    private String privateKeyString;

    @Value("${public.key}")
    private String publicKeyString;


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

        return Base64.getEncoder().encodeToString(hashedPassword);
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
            logger.error("Error getting private key {}", e);
        }

        // Decrypt ciphertext
        try {
            byte[] cipherBytes = decoder.decode(ciphertext);
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptBytes = decryptCipher.doFinal(cipherBytes);
            plaintext = new String(decryptBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Error decrypting password{}", e);
        }

        return plaintext;
    }

    public String rsaEncrypt(String plaintext) {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();
        String ciphertext = null;
        PublicKey publicKey = null;

        logger.info("private key string: {}", privateKeyString);
        logger.info("public key string: {}", publicKeyString);
        try {
            if (publicKeyString == null) {
                logger.error("publicKeyString is null! Check Spring property injection and bean instantiation.");
                throw new IllegalStateException("publicKeyString is null");
            }
            logger.info("public key string: {}", publicKeyString);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            byte[] publicKeyByte = decoder.decode(publicKeyString);
            publicKey = factory.generatePublic(new X509EncodedKeySpec(publicKeyByte));
        } catch (Exception e) {
            logger.error("Error getting public key {}", e);
        }

        // Encrypt plaintext
        try {
            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] secretByte = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] encByte = encryptCipher.doFinal(secretByte);
            ciphertext = encoder.encodeToString(encByte);
        } catch (Exception e) {
            logger.error("Error ecrypting password {}", e);
        }

        return ciphertext;
    }
}
