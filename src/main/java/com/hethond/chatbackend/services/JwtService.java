package com.hethond.chatbackend.services;

import com.hethond.chatbackend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;
    private final long expirationMillis;

    @Autowired
    private JwtService(@Value("${app.jwtSecret}") String secretKey,
                      @Value("${app.jwtExpirationMillis}") long expirationMillis) {
        this.secretKey = secretKey;
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(@NonNull User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(@NonNull Map<String, Object> extraClaims,
                                @NonNull User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("user_id", user.getId())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSecretKey())
                .compact();
    }

    // TODO -- Maybe validate token against a user

    public boolean isTokenExpired(@NonNull String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(@NonNull String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public long extractUserId(@NonNull String token) {
        return extractClaim(token, claims -> claims.get("user_id", Long.class));
    }

    public Date extractExpiration(@NonNull String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(@NonNull String token, @NonNull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(@NonNull String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    private SecretKey getSecretKey() {
        final byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
