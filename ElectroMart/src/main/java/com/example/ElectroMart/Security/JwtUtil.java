package com.example.ElectroMart.Security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secret;

    // Constructor-based initialization
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate a JWT token
    public String generateToken(String email) {
        return io.jsonwebtoken.Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secret, SignatureAlgorithm.HS256) // Use Key object
                .compact();
    }

    // Extract email (subject) from token
    public String extractEmail(String token) {
        return io.jsonwebtoken.Jwts.parserBuilder()
                .setSigningKey(secret) // Use Key object
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if token is valid
    public boolean isTokenValid(String token, String email) {
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return io.jsonwebtoken.Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    // Getter for the secret key (optional, for external use)
    public Key getSecretKey() {
        return secret;
    }
}
