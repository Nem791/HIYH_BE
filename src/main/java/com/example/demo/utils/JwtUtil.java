package com.example.demo.utils;

import com.example.demo.constants.AppConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey SIGNING_KEY;
    private final long defaultExpiryMs = AppConstants.JWT_EXPIRY_24_HOURS_MS;

    public JwtUtil(@Value("${JWT_SECRET}") String secretKey) {
        this.SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + defaultExpiryMs))
                .signWith(SIGNING_KEY)
                .compact();
    }

    public String generateSignupToken(String email, long customExpiryMs) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + customExpiryMs))
                .claim("purpose", "signup")
                .signWith(SIGNING_KEY)
                .compact();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SIGNING_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Date expiration = claims.getExpiration();
        System.out.println(expiration);
        if (expiration != null && expiration.before(new Date())) {
            throw new ExpiredJwtException(null, claims, "Token expired");
        }

        return true;
    }
}