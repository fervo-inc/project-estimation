package io.fervo.takecost.projectestimation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtils {

    private final JwtParser jwtParser;
    private final String secretKey;
    private final long tokenValidity;

    public JwtUtils(@Value("${security.jwt.secret}") String secretKey,
                    @Value("${security.jwt.token-expiration}") long tokenValidity) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("JWT secret key must not be null or blank.");
        }
        this.secretKey = secretKey;
        this.tokenValidity = tokenValidity;

        SecretKey signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.jwtParser = Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer("TakeCost")
                .requireAudience("TakeCostClient")
                .build();
    }


    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            return (List<String>) claims.get("roles");
        });
    }

    public String generateToken(UserDetails userDetails) {
        var now = new Date();
        var expiration = new Date(now.getTime() + tokenValidity);

        return Jwts.builder()
                .issuer("TakeCost")
                .subject(userDetails.getUsername())
                .audience().add("TakeCostClient").and()
                .issuedAt(now)
                .notBefore(now)
                .expiration(expiration)
                .id(UUID.randomUUID().toString())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                        .toList())
                .signWith(getSigningKey())
                .compact();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

}
