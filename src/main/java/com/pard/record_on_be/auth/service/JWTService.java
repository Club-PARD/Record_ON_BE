package com.pard.record_on_be.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
    private Key key;

    @Value("${jwt.secret}")
    public void setKey(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Getter
    @Value("${jwt.access.token.expiration}")
    private Long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh.token.expiration}")
    private Long refreshTokenExpiration;

    public String generateAccessToken(String email) {
        return createToken(email, accessTokenExpiration);
    }

    public String generateRefreshToken(String email) {
        return createToken(email, refreshTokenExpiration);
    }

    public String refreshAccessToken(String refreshToken) throws JwtException {
        try {
            Claims claims = getClaimsFromToken(refreshToken);
            return generateAccessToken(claims.getSubject());
        } catch (JwtException e) {
            throw new JwtException("Failed to validate refresh token: " + e.getMessage());
        }
    }

    private String createToken(String email, Long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

}
