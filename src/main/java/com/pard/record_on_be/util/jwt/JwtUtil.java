package com.pard.record_on_be.utill.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "4261656C64756E67";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
//    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//
//
//    private SecretKey getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

//    private String createJwtToken(String email) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
//
//        return Jwts.builder()
//                .signWith(key) // Secret key와 알고리즘을 명시적으로 사용
//                .subject(email)
//                .issuedAt(now)
//                .expiration(expiryDate)
//                .compact();
//    }


//    private Claims extractAllClaims(String token){
//        return Jwts.parser()
//                .verifyWith(this.getSigningKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
