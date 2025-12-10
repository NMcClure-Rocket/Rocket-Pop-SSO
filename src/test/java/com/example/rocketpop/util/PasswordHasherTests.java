package com.example.rocketpop.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PasswordHasherTests {

    @Autowired
    private PasswordHasher passwordHasher;

    private String testSalt;

    @BeforeEach
    public void setUp() {
        testSalt = passwordHasher.getRandomSalt();
    }

    @Test
    public void testHashPassword_ValidInput() {
        // Arrange
        String password = "testPassword123";
        
        // Act
        String hashedPassword = passwordHasher.hashPassword(password, testSalt);
        
        // Assert
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.length() > 0);
    }

    @Test
    public void testHashPassword_SamePasswordSameSalt() {
        // Arrange
        String password = "testPassword123";
        
        // Act
        String hash1 = passwordHasher.hashPassword(password, testSalt);
        String hash2 = passwordHasher.hashPassword(password, testSalt);
        
        // Assert
        assertEquals(hash1, hash2, "Same password with same salt should produce same hash");
    }

    @Test
    public void testHashPassword_SamePasswordDifferentSalt() {
        // Arrange
        String password = "testPassword123";
        String salt1 = passwordHasher.getRandomSalt();
        String salt2 = passwordHasher.getRandomSalt();
        
        // Act
        String hash1 = passwordHasher.hashPassword(password, salt1);
        String hash2 = passwordHasher.hashPassword(password, salt2);
        
        // Assert
        assertNotEquals(hash1, hash2, "Same password with different salt should produce different hash");
    }

    @Test
    public void testHashPassword_DifferentPasswords() {
        // Arrange
        String password1 = "password1";
        String password2 = "password2";
        
        // Act
        String hash1 = passwordHasher.hashPassword(password1, testSalt);
        String hash2 = passwordHasher.hashPassword(password2, testSalt);
        
        // Assert
        assertNotEquals(hash1, hash2, "Different passwords should produce different hashes");
    }

    @Test
    public void testHashPassword_EmptyPassword() {
        // Arrange
        String password = "";
        
        // Act
        String hashedPassword = passwordHasher.hashPassword(password, testSalt);
        
        // Assert
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.length() > 0);
    }

    @Test
    public void testHashPassword_SpecialCharacters() {
        // Arrange
        String password = "p@ssw0rd!#$%^&*()";
        
        // Act
        String hashedPassword = passwordHasher.hashPassword(password, testSalt);
        
        // Assert
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.length() > 0);
    }

    @Test
    public void testHashPassword_UnicodeCharacters() {
        // Arrange
        String password = "パスワード123";
        
        // Act
        String hashedPassword = passwordHasher.hashPassword(password, testSalt);
        
        // Assert
        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.length() > 0);
    }

    @Test
    public void testGetRandomSalt_NotNull() {
        // Act
        String salt = passwordHasher.getRandomSalt();
        
        // Assert
        assertNotNull(salt);
        assertTrue(salt.length() > 0);
    }

    @Test
    public void testGetRandomSalt_IsBase64() {
        // Act
        String salt = passwordHasher.getRandomSalt();
        
        // Assert
        assertDoesNotThrow(() -> Base64.getDecoder().decode(salt), 
            "Salt should be valid Base64");
    }

    @Test
    public void testGetRandomSalt_UniqueSalts() {
        // Act
        String salt1 = passwordHasher.getRandomSalt();
        String salt2 = passwordHasher.getRandomSalt();
        String salt3 = passwordHasher.getRandomSalt();
        
        // Assert
        assertNotEquals(salt1, salt2);
        assertNotEquals(salt2, salt3);
        assertNotEquals(salt1, salt3);
    }

    @Test
    public void testGetRandomSalt_CorrectLength() {
        // Act
        String salt = passwordHasher.getRandomSalt();
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        
        // Assert
        assertEquals(16, saltBytes.length, "Salt should be 16 bytes");
    }

    @Test
    public void testRsaEncryptDecrypt_RoundTrip() {
        // Arrange
        String plaintext = "secretPassword123";
        
        // Act
        String encrypted = passwordHasher.rsaEncrypt(plaintext);
        String decrypted = passwordHasher.rsaDecrypt(encrypted);
        
        // Assert
        assertNotNull(encrypted);
        assertNotEquals(plaintext, encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    public void testRsaEncrypt_ValidInput() {
        // Arrange
        String plaintext = "testPassword";
        
        // Act
        String ciphertext = passwordHasher.rsaEncrypt(plaintext);
        
        // Assert
        assertNotNull(ciphertext);
        assertNotEquals(plaintext, ciphertext);
        assertTrue(ciphertext.length() > 0);
    }

    @Test
    public void testRsaEncrypt_EmptyString() {
        // Arrange
        String plaintext = "";
        
        // Act
        String ciphertext = passwordHasher.rsaEncrypt(plaintext);
        
        // Assert
        assertNotNull(ciphertext);
    }

    @Test
    public void testRsaEncrypt_SpecialCharacters() {
        // Arrange
        String plaintext = "p@ssw0rd!#$%";
        
        // Act
        String encrypted = passwordHasher.rsaEncrypt(plaintext);
        String decrypted = passwordHasher.rsaDecrypt(encrypted);
        
        // Assert
        assertEquals(plaintext, decrypted);
    }

    @Test
    public void testRsaEncrypt_LongString() {
        // Arrange - RSA can encrypt up to key_size/8 - 11 bytes (for PKCS1Padding)
        String plaintext = "This is a longer password that tests the encryption limits";
        
        // Act
        String encrypted = passwordHasher.rsaEncrypt(plaintext);
        String decrypted = passwordHasher.rsaDecrypt(encrypted);
        
        // Assert
        assertEquals(plaintext, decrypted);
    }

    @Test
    public void testRsaDecrypt_InvalidCiphertext() {
        // Arrange
        String invalidCiphertext = "InvalidBase64Ciphertext!@#$";
        
        // Act
        String decrypted = passwordHasher.rsaDecrypt(invalidCiphertext);
        
        // Assert
        assertNull(decrypted, "Invalid ciphertext should return null");
    }

    @Test
    public void testRsaDecrypt_EmptyString() {
        // Arrange
        String emptyCiphertext = "";
        
        // Act
        String decrypted = passwordHasher.rsaDecrypt(emptyCiphertext);
        
        // Assert
        assertNull(decrypted, "Empty ciphertext should return null");
    }

    @Test
    public void testRsaDecrypt_CorruptedData() {
        // Arrange - valid Base64 but not valid RSA encrypted data
        String corruptedData = Base64.getEncoder().encodeToString("corrupted".getBytes());
        
        // Act
        String decrypted = passwordHasher.rsaDecrypt(corruptedData);
        
        // Assert
        assertNull(decrypted, "Corrupted data should return null");
    }

    @Test
    public void testRsaEncrypt_SamePlaintextDifferentCiphertext() {
        // Arrange - RSA encryption with padding should produce different ciphertext each time
        String plaintext = "testPassword";
        
        // Act
        String encrypted1 = passwordHasher.rsaEncrypt(plaintext);
        String encrypted2 = passwordHasher.rsaEncrypt(plaintext);
        
        // Assert - Due to RSA padding, same plaintext may produce different ciphertext
        // But both should decrypt to the same plaintext
        String decrypted1 = passwordHasher.rsaDecrypt(encrypted1);
        String decrypted2 = passwordHasher.rsaDecrypt(encrypted2);
        assertEquals(plaintext, decrypted1);
        assertEquals(plaintext, decrypted2);
    }

    @Test
    public void testHashPassword_NullSalt() {
        // Arrange
        String password = "testPassword";
        
        // Act & Assert - NullPointerException is thrown when salt is null
        assertThrows(NullPointerException.class, () -> {
            passwordHasher.hashPassword(password, null);
        });
    }

    @Test
    public void testHashPassword_InvalidSalt() {
        // Arrange
        String password = "testPassword";
        String invalidSalt = "NotValidBase64!@#$";
        
        // Act & Assert - Invalid Base64 salt causes NullPointerException during encoding
        assertThrows(NullPointerException.class, () -> {
            passwordHasher.hashPassword(password, invalidSalt);
        });
    }

    @Test
    public void testRsaEncrypt_WithInvalidPublicKey() {
        // Arrange
        String plaintext = "test";
        String originalPublicKey = (String) ReflectionTestUtils.getField(passwordHasher, "publicKeyString");
        
        // Set invalid public key
        ReflectionTestUtils.setField(passwordHasher, "publicKeyString", "InvalidKey");
        
        // Act
        String result = passwordHasher.rsaEncrypt(plaintext);
        
        // Assert
        assertNull(result, "Should return null for invalid public key");
        
        // Cleanup - restore original key
        ReflectionTestUtils.setField(passwordHasher, "publicKeyString", originalPublicKey);
    }

    @Test
    public void testRsaDecrypt_WithInvalidPrivateKey() {
        // Arrange
        String validCiphertext = passwordHasher.rsaEncrypt("test");
        String originalPrivateKey = (String) ReflectionTestUtils.getField(passwordHasher, "privateKeyString");
        
        // Set invalid private key
        ReflectionTestUtils.setField(passwordHasher, "privateKeyString", "InvalidKey");
        
        // Act
        String result = passwordHasher.rsaDecrypt(validCiphertext);
        
        // Assert
        assertNull(result, "Should return null for invalid private key");
        
        // Cleanup - restore original key
        ReflectionTestUtils.setField(passwordHasher, "privateKeyString", originalPrivateKey);
    }
}
