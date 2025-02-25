package com.example.ElectroMart.Security;

import com.example.ElectroMart.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMillis;

    // ✅ Constructor: Initialize Secret Key & Expiration
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    // ✅ Generate JWT Token with Roles & User ID
    public String generateToken(User user, List<String> roles) {
        String token = Jwts.builder()
                .subject(user.getEmail())  // ✅ Set Subject as Email
                .claim("userId", user.getId()) // ✅ Include User ID
                .claim("name", user.getUserName()) // ✅ Include Username
                .claim("roles", roles) // ✅ Include Roles
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey)
                .compact();

        // ✅ Debugging: Print Token Details
        System.out.println("🔐 JWT Token Generated:");
        System.out.println("  - Email (sub): " + user.getEmail());
        System.out.println("  - User ID: " + user.getId());
        System.out.println("  - Name: " + user.getUserName());
        System.out.println("  - Roles: " + roles);
        System.out.println("  - Token: " + token);

        return token;
    }

    // ✅ Extract Email (Subject) from Token
    public String extractEmail(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // ✅ Extract User ID from Token
    public String extractUserId(String token) {
        return getClaimsFromToken(token).get("userId", String.class);
    }

    // ✅ Extract Roles from Token
    public List<String> extractRoles(String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }

    // ✅ Validate Token: Check Expiration & Subject
    public boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    // ✅ Check If Token is Expired
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    // ✅ Parse Claims from Token (JJWT 0.12+ Fix)
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey) // ✅ Verify with Secret Key
                    .build()
                    .parseSignedClaims(token)
                    .getPayload(); // ✅ Extract Payload
        } catch (Exception e) {
            throw new RuntimeException("❌ Invalid JWT Token: " + e.getMessage());
        }
    }

    // ✅ Extract Username from Security Context
    public String extractUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("No user is currently authenticated");
    }

    // ✅ Getter for Secret Key
    public SecretKey getSecretKey() {
        return secretKey;
    }
}
