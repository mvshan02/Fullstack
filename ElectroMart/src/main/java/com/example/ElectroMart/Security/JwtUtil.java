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

    // Constructor-based initialization
    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // ✅ Fix: Correct key usage
        this.expirationMillis = expirationMillis;
    }

    // ✅ Generate a JWT token with roles
    // ✅ Corrected method: Pass a `User` object instead of just `email`
    public String generateToken(User user, List<String> roles) {
        String token = Jwts.builder()
                .subject(user.getEmail())  // ✅ Email as subject
                .claim("name", user.getUserName())  // ✅ Ensure name is included
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey)
                .compact();

        // ✅ Debugging: Print claims before returning the token
        System.out.println("JWT Claims:");
        System.out.println("  - Email (sub): " + user.getEmail());
        System.out.println("  - Name: " + user.getUserName());  // Check if this prints correctly
        System.out.println("  - Roles: " + roles);
        System.out.println("  - Token: " + token);

        return token;
    }



    // ✅ Extract email (subject) from token
    public String extractEmail(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // ✅ Extract roles from token
    public List<String> extractRoles(String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }

    // ✅ Validate token by checking expiration and subject
    public boolean isTokenValid(String token, String email) {
        return email.equals(extractEmail(token)) && !isTokenExpired(token);
    }

    // ✅ Check if token is expired
    private boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    // ✅ Parse claims from token (Fixed for JJWT 0.12.5)
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // ✅ Fix: Correct parsing method
                .build() // ✅ Fix: Add .build()
                .parseSignedClaims(token) // ✅ Fix: Use parseSignedClaims() instead of parseClaimsJws()
                .getPayload(); // ✅ Fix: Use getPayload() instead of getBody()
    }

    // ✅ Extract username from SecurityContext
    public String extractUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("No user is currently authenticated");
    }

    // ✅ Getter for secret key
    public SecretKey getSecretKey() {
        return secretKey;
    }
}
