package com.example.rocketpop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.example.rocketpop.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    
    // Secret key for user tokens (can be used with external apps)
    private static final String USER_SECRET = "RockertSoftwareRocks2025ThisIsNotSecureEnough";
    
    // Different secret for admin tokens (internal use only)
    private static final String ADMIN_SECRET = "RocketSSOAdminSecretKey2025InternalUseOnly!!";
    
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    
    private SecretKey getUserSecretKey() {
        return Keys.hmacShaKeyFor(USER_SECRET.getBytes(StandardCharsets.UTF_8));
    }
    
    private SecretKey getAdminSecretKey() {
        return Keys.hmacShaKeyFor(ADMIN_SECRET.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * Generate token for regular users (with roles 'user' or 'manager')
     * These tokens can be used by external applications
     */
    public String generateUserToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("location", user.getLocation());
        claims.put("department", user.getDepartment());
        claims.put("title", user.getTitle());
        
        return Jwts.builder()
                .setIssuer("Auth Service")
                .setClaims(claims)
                .setSubject(user.getFirstName() + " " + user.getLastName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getUserSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Generate token for admin users
     * These tokens are for internal SSO website use only
     */
    public String generateAdminToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("location", user.getLocation());
        claims.put("department", user.getDepartment());
        claims.put("title", user.getTitle());
        claims.put("role", "admin");
        claims.put("type", "admin");
        
        return Jwts.builder()
        .setIssuer("Auth Service")
                .setClaims(claims)
                .setSubject(user.getFirstName() + " " + user.getLastName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getUserSecretKey(), SignatureAlgorithm.HS256)
                .compact();
                
                // .setClaims(claims)
                // .setIssuedAt(new Date(System.currentTimeMillis()))
                // .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // .signWith(getAdminSecretKey(), SignatureAlgorithm.HS256)
                // .compact();
    }
    
    /**
     * Extract username from token
     */
    public String extractUsername(String token, boolean isAdmin) {
        return extractClaims(token, isAdmin).getSubject();
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
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * Validate token
     */
    public boolean validateToken(String token, boolean isAdmin) {
        try {
            Claims claims = extractClaims(token, isAdmin);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if token is admin token
     */
    public boolean isAdminToken(String token) {
        try {
            Claims claims = extractClaims(token, true);
            return "admin".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Clean Bearer prefix from token
     */
    public String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
