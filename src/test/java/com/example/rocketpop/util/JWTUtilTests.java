package com.example.rocketpop.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class JWTUtilTests {

    @Autowired
    private JWTUtil jwtUtil;

    private String validUserToken;
    private String validAdminToken;
    private String expiredUserToken;
    private String expiredAdminToken;

    @BeforeEach
    public void setUp() {
        // Generate valid tokens
        validUserToken = jwtUtil.generateUserToken("testuser", "test@example.com", "manager");
        validAdminToken = jwtUtil.generateAdminToken("adminuser", "admin@example.com");

        // Generate expired tokens for testing
        expiredUserToken = generateExpiredUserToken("expireduser", "expired@example.com", "user");
        expiredAdminToken = generateExpiredAdminToken("expiredadmin", "expiredadmin@example.com");
    }

    // Helper methods to generate expired tokens
    private String generateExpiredUserToken(String username, String email, String title) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", title);
        claims.put("type", "user");
        
        SecretKey key = Keys.hmacShaKeyFor("RockertSoftwareRocks2025ThisIsNotSecureEnough".getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredAdminToken(String username, String email) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("role", "admin");
        claims.put("type", "admin");
        
        SecretKey key = Keys.hmacShaKeyFor("RocketSSOAdminSecretKey2025InternalUseOnly!!".getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ========== generateUserToken Tests ==========

    @Test
    public void testGenerateUserToken_ValidInput() {
        String token = jwtUtil.generateUserToken("john", "john@example.com", "user");
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    public void testGenerateUserToken_ManagerRole() {
        String token = jwtUtil.generateUserToken("manager", "manager@example.com", "manager");
        assertNotNull(token);
        String email = jwtUtil.extractEmail(token, false);
        assertEquals("manager@example.com", email);
    }

    // ========== generateAdminToken Tests ==========

    @Test
    public void testGenerateAdminToken_ValidInput() {
        String token = jwtUtil.generateAdminToken("adminuser", "admin@example.com");
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    public void testGenerateAdminToken_ContainsCorrectType() {
        String token = jwtUtil.generateAdminToken("adminuser", "admin@example.com");
        assertTrue(jwtUtil.isAdminToken(token));
    }

    // ========== extractUsername Tests ==========

    @Test
    public void testExtractUsername_UserToken() {
        String username = jwtUtil.extractUsername(validUserToken, false);
        assertEquals("testuser", username);
    }

    @Test
    public void testExtractUsername_AdminToken() {
        String username = jwtUtil.extractUsername(validAdminToken, true);
        assertEquals("adminuser", username);
    }

    @Test
    public void testExtractUsername_WrongSecretFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(validUserToken, true); // Using admin secret for user token
        });
    }

    // ========== extractEmail Tests ==========

    @Test
    public void testExtractEmail_UserToken() {
        String email = jwtUtil.extractEmail(validUserToken, false);
        assertEquals("test@example.com", email);
    }

    @Test
    public void testExtractEmail_AdminToken() {
        String email = jwtUtil.extractEmail(validAdminToken, true);
        assertEquals("admin@example.com", email);
    }

    @Test
    public void testExtractEmail_WrongSecretFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractEmail(validAdminToken, false); // Using user secret for admin token
        });
    }

    // ========== extractTitle Tests ==========

    @Test
    public void testExtractTitle_UserToken() {
        String title = jwtUtil.extractTitle(validUserToken, false);
        // Note: extractTitle returns null because we store "role" not "title"
        assertNull(title);
    }

    @Test
    public void testExtractTitle_AdminToken() {
        String title = jwtUtil.extractTitle(validAdminToken, true);
        assertNull(title); // Admin tokens also don't have "title" claim
    }

    // ========== validateToken Tests ==========

    @Test
    public void testValidateToken_ValidUserToken() {
        boolean isValid = jwtUtil.validateToken(validUserToken, false);
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_ValidAdminToken() {
        boolean isValid = jwtUtil.validateToken(validAdminToken, true);
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_ExpiredUserToken() {
        boolean isValid = jwtUtil.validateToken(expiredUserToken, false);
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_ExpiredAdminToken() {
        boolean isValid = jwtUtil.validateToken(expiredAdminToken, true);
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        boolean isValid = jwtUtil.validateToken("invalid.token.here", false);
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_NullToken() {
        boolean isValid = jwtUtil.validateToken(null, false);
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_EmptyToken() {
        boolean isValid = jwtUtil.validateToken("", false);
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_MalformedToken() {
        boolean isValid = jwtUtil.validateToken("not.a.valid.jwt.token", false);
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_WrongSecret() {
        boolean isValid = jwtUtil.validateToken(validUserToken, true);
        assertFalse(isValid);
    }

    // ========== isAdminToken Tests ==========

    @Test
    public void testIsAdminToken_ValidAdminToken() {
        boolean isAdmin = jwtUtil.isAdminToken(validAdminToken);
        assertTrue(isAdmin);
    }

    @Test
    public void testIsAdminToken_UserToken() {
        boolean isAdmin = jwtUtil.isAdminToken(validUserToken);
        assertFalse(isAdmin); // User tokens can't be validated with admin secret
    }

    @Test
    public void testIsAdminToken_InvalidToken() {
        boolean isAdmin = jwtUtil.isAdminToken("invalid.token");
        assertFalse(isAdmin);
    }

    @Test
    public void testIsAdminToken_NullToken() {
        boolean isAdmin = jwtUtil.isAdminToken(null);
        assertFalse(isAdmin);
    }

    @Test
    public void testIsAdminToken_EmptyToken() {
        boolean isAdmin = jwtUtil.isAdminToken("");
        assertFalse(isAdmin);
    }

    // ========== cleanToken Tests ==========

    @Test
    public void testCleanToken_WithBearerPrefix() {
        String tokenWithBearer = "Bearer " + validUserToken;
        String cleaned = jwtUtil.cleanToken(tokenWithBearer);
        assertEquals(validUserToken, cleaned);
    }

    @Test
    public void testCleanToken_WithoutBearerPrefix() {
        String cleaned = jwtUtil.cleanToken(validUserToken);
        assertEquals(validUserToken, cleaned);
    }

    @Test
    public void testCleanToken_NullToken() {
        String cleaned = jwtUtil.cleanToken(null);
        assertNull(cleaned);
    }

    @Test
    public void testCleanToken_EmptyToken() {
        String cleaned = jwtUtil.cleanToken("");
        assertEquals("", cleaned);
    }

    @Test
    public void testCleanToken_OnlyBearer() {
        String cleaned = jwtUtil.cleanToken("Bearer");
        assertEquals("Bearer", cleaned); // Doesn't have space after
    }

    @Test
    public void testCleanToken_BearerWithoutSpace() {
        String token = "Bearer" + validUserToken;
        String cleaned = jwtUtil.cleanToken(token);
        assertEquals(token, cleaned); // No space, so not cleaned
    }

    @Test
    public void testCleanToken_MultipleBearers() {
        String token = "Bearer Bearer " + validUserToken;
        String cleaned = jwtUtil.cleanToken(token);
        assertEquals("Bearer " + validUserToken, cleaned); // Only removes first "Bearer "
    }

    // ========== Integration Tests ==========

    @Test
    public void testFullUserFlow_GenerateValidateExtract() {
        // Generate
        String token = jwtUtil.generateUserToken("integrationuser", "integration@test.com", "user");
        
        // Validate
        assertTrue(jwtUtil.validateToken(token, false));
        
        // Extract
        assertEquals("integrationuser", jwtUtil.extractUsername(token, false));
        assertEquals("integration@test.com", jwtUtil.extractEmail(token, false));
        
        // Clean
        String withBearer = "Bearer " + token;
        assertEquals(token, jwtUtil.cleanToken(withBearer));
    }

    @Test
    public void testFullAdminFlow_GenerateValidateExtract() {
        // Generate
        String token = jwtUtil.generateAdminToken("integrationadmin", "adminintegration@test.com");
        
        // Validate
        assertTrue(jwtUtil.validateToken(token, true));
        assertTrue(jwtUtil.isAdminToken(token));
        
        // Extract
        assertEquals("integrationadmin", jwtUtil.extractUsername(token, true));
        assertEquals("adminintegration@test.com", jwtUtil.extractEmail(token, true));
        
        // Clean
        String withBearer = "Bearer " + token;
        assertEquals(token, jwtUtil.cleanToken(withBearer));
    }

    @Test
    public void testTokenExpirationValidation() {
        // Valid token should be valid
        assertTrue(jwtUtil.validateToken(validUserToken, false));
        
        // Expired token should be invalid
        assertFalse(jwtUtil.validateToken(expiredUserToken, false));
    }

    @Test
    public void testDifferentSecretsForUserAndAdmin() {
        // User token with user secret: valid
        assertTrue(jwtUtil.validateToken(validUserToken, false));
        
        // User token with admin secret: invalid
        assertFalse(jwtUtil.validateToken(validUserToken, true));
        
        // Admin token with admin secret: valid
        assertTrue(jwtUtil.validateToken(validAdminToken, true));
        
        // Admin token with user secret: invalid
        assertFalse(jwtUtil.validateToken(validAdminToken, false));
    }
}
