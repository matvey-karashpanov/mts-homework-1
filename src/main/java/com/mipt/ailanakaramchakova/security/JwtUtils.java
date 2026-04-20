package com.mipt.ailanakaramchakova.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    private final JwtProperties jwtProperties;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(String username, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.accessTtlSeconds());

        return Jwts.builder()
          .issuer(jwtProperties.issuer())
          .subject(username)
          .issuedAt(Date.from(now))
          .expiration(Date.from(expiration))
          .claims(claims)
          .signWith(jwtProperties.getSigningKey(), Jwts.SIG.HS256)
          .compact();
    }

    public Jws<Claims> parseAndValidate(String token) {
        JwtParser parser = Jwts.parser()
          .verifyWith(jwtProperties.getSigningKey())
          .requireIssuer(jwtProperties.issuer())
          .build();
        return parser.parseSignedClaims(token);
    }
}
