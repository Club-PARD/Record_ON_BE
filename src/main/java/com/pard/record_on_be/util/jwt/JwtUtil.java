package com.pard.record_on_be.util.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "4261656C64756E67"; // 16 바이트 길이의 키 필요
    public static final long ACCESS_EXPIRATION_TIME = 1000 * 60 * 15; // 15 minutes
    public static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

    public String generateAccessToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + ACCESS_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + REFRESH_EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith( SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
