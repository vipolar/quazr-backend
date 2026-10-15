package io.quazr.backend.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.Map;

@Service
public class AuxiliaryJwtService {
    private final Integer jwtExpirationHours;
    private final SecretKey jwtSecretKey;
    private final JwtParser jwtParser;

    public AuxiliaryJwtService(@Value("${app.security.jwt.secret}") String secret, @Value("${app.security.jwt.expiration-hours:24}") int expirationHours) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationHours = expirationHours;

        this.jwtParser = Jwts.parser()
            .verifyWith(jwtSecretKey)
            .build();
    }

    public Map<String, Object> generate(UUID userUUID) {
        Instant now = Instant.now();
        Instant expirationInstant = now.plus(jwtExpirationHours, ChronoUnit.HOURS);
        
        String jwtSignedTemporaryToken = Jwts.builder()
            .expiration(Date.from(expirationInstant))
            .claim("role", "guest")
            .subject(userUUID.toString())
            .issuedAt(Date.from(now))
            .signWith(jwtSecretKey)
            .compact();

        return Map.of(
            "token", jwtSignedTemporaryToken,
            "expiresIn", jwtExpirationHours * 3600,
            "expiresAt", expirationInstant.toEpochMilli()
        );
    }

    public Jwt decode(String token) {
        Claims claims = this.jwtParser.parseSignedClaims(token).getPayload();

        return Jwt.withTokenValue(token)
                .subject(claims.getSubject())
                .header("alg", "HS256")
                .issuedAt(claims.getIssuedAt().toInstant())
                .expiresAt(claims.getExpiration().toInstant())
                .claim("role", "guest")
                .build();
    }
}
