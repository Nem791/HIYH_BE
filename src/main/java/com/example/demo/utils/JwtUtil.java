package com.example.demo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "your-256-bit-secret-key-should-be-long-enough";
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long EXPIRATION_MS = 86400000; // 24 hours

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SIGNING_KEY)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SIGNING_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        // Optional: check expiration explicitly (usually parseSignedClaims does this)
        Date expiration = claims.getExpiration();
        if (expiration != null && expiration.before(new Date())) {
            throw new ExpiredJwtException(null, claims, "Token expired");
        }

        return true;
    }
}