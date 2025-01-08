package com.example.ElectroMart.Config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}") // Inject the secret key from application.properties
    private String secretKey;

    private static Key SECRET_KEY;

    @PostConstruct
    public void init() {
        // Convert the secret key into a secure Key object
        SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public static Key getSecretKey() {
        return SECRET_KEY;
    }
}
