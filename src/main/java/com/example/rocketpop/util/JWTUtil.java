package com.example.rocketpop.util;

import org.json.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.example.rocketpop.model.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWTUtil {

    
    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
    
    // Secret key for user tokens (can be used with external apps)
    private static final String USER_SECRET = "RockertSoftwareRocks2025ThisIsNotSecureEnough";
    
    // Different secret for admin tokens (internal use only)
    private static final String ADMIN_SECRET = "RocketSSOAdminSecretKey2025InternalUseOnlygibberishasdfasdfasdf";
    
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    

    public SecretKey getAdminSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(ADMIN_SECRET));
    }
    
    public SecretKey getUserSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(USER_SECRET));
    }
    
    /**
     * Generate token for regular users (with roles 'user' or 'manager')
     * These tokens can be used by external applications
     */
    public String generateUserToken(User user) {

        JSONObject userObject = new JSONObject();
        userObject.put("id", user.getId());
        userObject.put("firstName", user.getFirstName());
        userObject.put("lastName", user.getLastName());
        userObject.put("location", user.getLocation());
        userObject.put("department", user.getDepartment());
        userObject.put("title", user.getTitle());

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("location", user.getLocation());
        claims.put("department", user.getDepartment());
        claims.put("title", user.getTitle());

        SecretKey key = getUserSecretKey();

        
        return Jwts.builder()
            .issuer("Auth Service")
            .claims(claims)
            .subject(user.getFirstName() + " " + user.getLastName())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact();
    }
    
    /**
     * Generate token for admin users
     * These tokens are for internal SSO website use only
     */
    public String generateAdminToken(User user) {

        JSONObject userObject = new JSONObject();
        userObject.put("id", user.getId());
        userObject.put("firstName", user.getFirstName());
        userObject.put("lastName", user.getLastName());
        userObject.put("location", user.getLocation());
        userObject.put("department", user.getDepartment());
        userObject.put("title", user.getTitle());
        userObject.put("role", "admin");
        userObject.put("type", "admin");

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("location", user.getLocation());
        claims.put("department", user.getDepartment());
        claims.put("title", user.getTitle());

        SecretKey key = getAdminSecretKey();
        
        return Jwts.builder()
            .issuer("Auth Service")
            .claims(claims)
            .subject(user.getFirstName() + " " + user.getLastName())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(key)
            .compact();
    }
    
    /**
     * Extract id from token
     */
    public String extractId(String token, boolean isAdmin) {
        Object id = extractClaims(token, isAdmin).get("id");
        return id != null ? id.toString() : null;
    }
    
    /**
     * Extract title from token
     */
    public String extractTitle(String token, boolean isAdmin) {
        return (String) extractClaims(token, isAdmin).get("title");
    }
    
    /**
     * Extract email from token
     */
    public String extractEmail(String token, boolean isAdmin) {
        return (String) extractClaims(token, isAdmin).get("email");
    }
    
    /**
     * Extract all claims from token
     */
    private Claims extractClaims(String token, boolean isAdmin) {
        SecretKey key = isAdmin ? getAdminSecretKey() : getUserSecretKey();
        logger.info("extractClaims called with token: {}", token);
        logger.info("key: {}", key);
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        logger.info("claims: {}", claims);
        return claims;
    }
    
    /**
     * Validate token
     */
    public boolean validateToken(String token, boolean isAdmin) {
        try {
            Claims claims = extractClaims(token, isAdmin);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("Error validating token", e);
            return false;
        }
    }
    
    /**
     * Check if token is admin token
     */
    public boolean isAdminToken(String token) {
        try {
            Claims claims = extractClaims(token, true);
            return "admin".equals(claims.get("title"));
        } catch (Exception e) {
            logger.error("Error checking if token is admin token {}", e);
            return false;
        }
    }
    
    /**
     * Clean Bearer prefix from token
     */
    public String cleanToken(String token) {
        logger.info("cleanToken called with token: {}", token);
        if (token != null && token.startsWith("Bearer ")) {
            logger.info("Bearer prefix found, removing", token.substring(7));
            return token.substring(7);
        }
        return token;
    }
}
