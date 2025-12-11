package com.example.rocketpop.util;

import com.example.rocketpop.model.User;
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
    private User testUser;
    private User adminUser;

    @BeforeEach
    public void setUp() {
        // Create test user
        testUser = new User("testuser", "hashedpassword", "salt");
        testUser.setId(1);
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setTitle("manager");
        testUser.setDepartment(1);
        testUser.setLocation(1);

        // Create admin user
        adminUser = new User("adminuser", "hashedadmin", "salt");
        adminUser.setId(2);
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setEmail("admin@example.com");
        adminUser.setTitle("admin");
        adminUser.setDepartment(1);
        adminUser.setLocation(1);

        // Generate valid tokens
        validUserToken = jwtUtil.generateUserToken(testUser);
        validAdminToken = jwtUtil.generateAdminToken(adminUser);

        // Generate expired tokens for testing
        expiredUserToken = generateExpiredUserToken(testUser);
        expiredAdminToken = generateExpiredAdminToken(adminUser);
    }

    // Helper methods to generate expired tokens
    private String generateExpiredUserToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("first_name", user.getFirstName());
        claims.put("last_name", user.getLastName());
        claims.put("location", Integer.toString(user.getLocation()));
        claims.put("department", Integer.toString(user.getDepartment()));
        claims.put("title", user.getTitle());
        
        SecretKey key = Keys.hmacShaKeyFor("RockertSoftwareRocks2025ThisIsNotSecureEnough".getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getFirstName() + " " + user.getLastName())
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredAdminToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("location", user.getLocation());
        claims.put("department", user.getDepartment());
        claims.put("title", user.getTitle());
        claims.put("role", "admin");
        claims.put("type", "admin");
        
        SecretKey key = Keys.hmacShaKeyFor("RocketSSOAdminSecretKey2025InternalUseOnly!!".getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getFirstName() + " " + user.getLastName())
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000000))
                .setExpiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ========== generateUserToken Tests ==========

    @Test
    public void testGenerateUserToken_ValidInput() {
        String token = jwtUtil.generateUserToken(testUser);
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    public void testGenerateUserToken_ManagerRole() {
        User manager = new User("manager", "pass", "salt");
        manager.setId(3);
        manager.setFirstName("Manager");
        manager.setLastName("Person");
        manager.setEmail("manager@example.com");
        manager.setTitle("manager");
        manager.setDepartment(2);
        manager.setLocation(2);
        
        String token = jwtUtil.generateUserToken(manager);
        assertNotNull(token);
        String title = jwtUtil.extractTitle(token, false);
        assertEquals("manager", title);
    }

    @Test
    public void testGenerateUserToken_ExtractsId() {
        String token = jwtUtil.generateUserToken(testUser);
        String id = jwtUtil.extractId(token, false);
        assertEquals("1", id);
    }

    // ========== generateAdminToken Tests ==========

    @Test
    public void testGenerateAdminToken_ValidInput() {
        String token = jwtUtil.generateAdminToken(adminUser);
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    public void testGenerateAdminToken_ContainsCorrectType() {
        String token = jwtUtil.generateAdminToken(adminUser);
        assertTrue(jwtUtil.isAdminToken(token));
    }

    @Test
    public void testGenerateAdminToken_ExtractsId() {
        String token = jwtUtil.generateAdminToken(adminUser);
        String id = jwtUtil.extractId(token, true);
        assertEquals("2", id);
    }

    // ========== extractId Tests ==========

    @Test
    public void testExtractId_UserToken() {
        String id = jwtUtil.extractId(validUserToken, false);
        assertEquals("1", id);
    }

    @Test
    public void testExtractId_AdminToken() {
        String id = jwtUtil.extractId(validAdminToken, true);
        assertEquals("2", id);
    }

    @Test
    public void testExtractId_WrongSecretFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractId(validUserToken, true); // Using admin secret for user token
        });
    }

    @Test
    public void testExtractId_ExpiredToken() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractId(expiredUserToken, false);
        });
    }

    // ========== extractEmail Tests ==========

    @Test
    public void testExtractEmail_UserToken() {
        String email = jwtUtil.extractEmail(validUserToken, false);
        assertNull(email); // Email is not included in JWT claims
    }

    @Test
    public void testExtractEmail_AdminToken() {
        String email = jwtUtil.extractEmail(validAdminToken, true);
        assertNull(email); // Email is not included in JWT claims
    }

    @Test
    public void testExtractEmail_WrongSecretFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractEmail(validAdminToken, false); // Using user secret for admin token
        });
    }

    @Test
    public void testExtractEmail_ExpiredTokenFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractEmail(expiredUserToken, false);
        });
    }

    // ========== extractTitle Tests ==========

    @Test
    public void testExtractTitle_UserToken() {
        String title = jwtUtil.extractTitle(validUserToken, false);
        assertEquals("manager", title);
    }

    @Test
    public void testExtractTitle_AdminToken() {
        String title = jwtUtil.extractTitle(validAdminToken, true);
        assertEquals("admin", title);
    }

    @Test
    public void testExtractTitle_WrongSecretFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractTitle(validUserToken, true);
        });
    }

    @Test
    public void testExtractTitle_ExpiredTokenFails() {
        assertThrows(Exception.class, () -> {
            jwtUtil.extractTitle(expiredUserToken, false);
        });
    }

    // ========== validateToken Tests ==========

    @Test
    public void testValidateToken_ValidUserToken() {
        assertTrue(jwtUtil.validateToken(validUserToken, false));
    }

    @Test
    public void testValidateToken_ValidAdminToken() {
        assertTrue(jwtUtil.validateToken(validAdminToken, true));
    }

    @Test
    public void testValidateToken_ExpiredUserToken() {
        assertFalse(jwtUtil.validateToken(expiredUserToken, false));
    }

    @Test
    public void testValidateToken_ExpiredAdminToken() {
        assertFalse(jwtUtil.validateToken(expiredAdminToken, true));
    }

    @Test
    public void testValidateToken_NullToken() {
        assertFalse(jwtUtil.validateToken(null, false));
    }

    @Test
    public void testValidateToken_EmptyToken() {
        assertFalse(jwtUtil.validateToken("", false));
    }

    @Test
    public void testValidateToken_InvalidToken() {
        assertFalse(jwtUtil.validateToken("invalid.token.string", false));
    }

    @Test
    public void testValidateToken_WrongSecret() {
        // User token validated with admin secret should fail
        assertFalse(jwtUtil.validateToken(validUserToken, true));
    }

    @Test
    public void testValidateToken_MalformedToken() {
        assertFalse(jwtUtil.validateToken("not-a-jwt-token", false));
    }

    @Test
    public void testValidateToken_TamperedToken() {
        String[] parts = validUserToken.split("\\.");
        if (parts.length == 3) {
            String tampered = parts[0] + ".tampered." + parts[2];
            assertFalse(jwtUtil.validateToken(tampered, false));
        }
    }

    // ========== isAdminToken Tests ==========

    @Test
    public void testIsAdminToken_UserToken() {
        assertFalse(jwtUtil.isAdminToken(validUserToken));
    }

    @Test
    public void testIsAdminToken_AdminToken() {
        assertTrue(jwtUtil.isAdminToken(validAdminToken));
    }

    @Test
    public void testIsAdminToken_InvalidToken() {
        assertFalse(jwtUtil.isAdminToken("invalid.token"));
    }

    @Test
    public void testIsAdminToken_NullToken() {
        assertFalse(jwtUtil.isAdminToken(null));
    }

    @Test
    public void testIsAdminToken_EmptyToken() {
        assertFalse(jwtUtil.isAdminToken(""));
    }

    // ========== cleanToken Tests ==========

    @Test
    public void testCleanToken_WithBearer() {
        String token = "Bearer " + validUserToken;
        String cleaned = jwtUtil.cleanToken(token);
        assertEquals(validUserToken, cleaned);
    }

    @Test
    public void testCleanToken_WithoutBearer() {
        String cleaned = jwtUtil.cleanToken(validUserToken);
        assertEquals(validUserToken, cleaned);
    }

    @Test
    public void testCleanToken_NullToken() {
        assertNull(jwtUtil.cleanToken(null));
    }

    @Test
    public void testCleanToken_EmptyToken() {
        String cleaned = jwtUtil.cleanToken("");
        assertEquals("", cleaned);
    }

    @Test
    public void testCleanToken_OnlyBearer() {
        String cleaned = jwtUtil.cleanToken("Bearer ");
        assertEquals("", cleaned);
    }

    @Test
    public void testCleanToken_LowercaseBearer() {
        String token = "bearer " + validUserToken;
        String cleaned = jwtUtil.cleanToken(token);
        // cleanToken only handles "Bearer " (capital B), not lowercase
        assertEquals(token, cleaned);
    }

    @Test
    public void testCleanToken_MixedCaseBearer() {
        String token = "BeArEr " + validUserToken;
        String cleaned = jwtUtil.cleanToken(token);
        // cleanToken only handles "Bearer " (capital B), not mixed case
        assertEquals(token, cleaned);
    }

    // ========== Integration Tests ==========

    @Test
    public void testTokenGenerationAndValidation_UserFlow() {
        User user = new User("john", "pass", "salt");
        user.setId(10);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setTitle("user");
        user.setDepartment(3);
        user.setLocation(3);
        
        String token = jwtUtil.generateUserToken(user);
        assertTrue(jwtUtil.validateToken(token, false));
        assertNull(jwtUtil.extractEmail(token, false)); // Email not in JWT claims
        assertEquals("10", jwtUtil.extractId(token, false));
        assertEquals("user", jwtUtil.extractTitle(token, false));
    }

    @Test
    public void testTokenGenerationAndValidation_AdminFlow() {
        User admin = new User("superadmin", "pass", "salt");
        admin.setId(100);
        admin.setFirstName("Super");
        admin.setLastName("Admin");
        admin.setEmail("super@example.com");
        admin.setTitle("admin");
        admin.setDepartment(0);
        admin.setLocation(0);
        
        String token = jwtUtil.generateAdminToken(admin);
        assertTrue(jwtUtil.validateToken(token, true));
        assertTrue(jwtUtil.isAdminToken(token));
        assertNull(jwtUtil.extractEmail(token, true)); // Email not in JWT claims
        assertEquals("100", jwtUtil.extractId(token, true));
    }

    @Test
    public void testTokenWithClean_Integration() {
        String bearerToken = "Bearer " + validUserToken;
        String cleaned = jwtUtil.cleanToken(bearerToken);
        assertTrue(jwtUtil.validateToken(cleaned, false));
        assertNull(jwtUtil.extractEmail(cleaned, false)); // Email not in JWT claims
    }
}
