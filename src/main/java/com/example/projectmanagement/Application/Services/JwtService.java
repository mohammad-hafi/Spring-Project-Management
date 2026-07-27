package com.example.projectmanagement.Application.Services;

import com.example.projectmanagement.Application.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtService {
    private final JwtProperties properties;
    private final SecretKey key;
    private final Clock clock;

    @Autowired
    public JwtService(JwtProperties properties) {
        this(properties, Clock.systemUTC());
    }

    public JwtService(JwtProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
        this.key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, Long userId, Set<String> permissions) {
        Instant issuedAt = clock.instant();
        return Jwts.builder()
                .subject(email)
                .issuer(properties.issuer())
                .id(UUID.randomUUID().toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(issuedAt.plus(properties.accessTokenTtl())))
                .claim("userId", userId)
                .claim("permissions", permissions.stream().sorted().toList())
                .signWith(key)
                .compact();
    }

    public JwtClaims parse(String token) {
        Claims claims = Jwts.parser().verifyWith(key).requireIssuer(properties.issuer())
                .build().parseSignedClaims(token).getPayload();
        Long userId = claims.get("userId", Long.class);
        String email = claims.getSubject();
        List<?> rawPermissions = claims.get("permissions", List.class);
        if (userId == null || email == null || email.isBlank() || rawPermissions == null
                || claims.getId() == null || claims.getIssuedAt() == null || claims.getExpiration() == null) {
            throw new IllegalArgumentException("JWT is missing required claims");
        }
        Set<String> permissions = new LinkedHashSet<>();
        rawPermissions.forEach(value -> {
            if (!(value instanceof String permission) || permission.isBlank()) {
                throw new IllegalArgumentException("JWT contains an invalid permission");
            }
            permissions.add(permission);
        });
        return new JwtClaims(userId, email, Set.copyOf(permissions));
    }
}
