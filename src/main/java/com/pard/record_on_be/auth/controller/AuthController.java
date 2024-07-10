package com.pard.record_on_be.auth.controller;

import com.pard.record_on_be.auth.dto.UserLoginRequestDTO;
import com.pard.record_on_be.auth.service.AuthService;
import com.pard.record_on_be.auth.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final JWTService jwtService;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "구글 로그인으로 받은 정보 넘겨주세요~")
    public Map<String, Object> googleLogin(@RequestBody UserLoginRequestDTO userLoginRequestDTO, HttpServletResponse response) {
        Map<String, Object> userInfo = authService.saveOrUpdateUser(userLoginRequestDTO);

        // 액세스 토큰 및 리프레시 토큰 발급
        String accessToken = jwtService.generateAccessToken(userLoginRequestDTO.getEmail());
        String refreshToken = jwtService.generateRefreshToken(userLoginRequestDTO.getEmail());

        // 액세스 토큰을 쿠키로 설정
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setHttpOnly(true); // 클라이언트 측에서 자바스크립트로 접근 불가
//        accessCookie.setSecure(true); // HTTPS 에서만 쿠키 전송
        accessCookie.setPath("/"); // 쿠키가 모든 경로에서 유효
        accessCookie.setMaxAge((int) (jwtService.getAccessTokenExpiration() / 1000)); // 쿠키 만료 시간 설정

        // 리프레시 토큰을 쿠키로 설정
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true); // 클라이언트 측에서 자바스크립트로 접근 불가
//        refreshCookie.setSecure(true); // HTTPS 에서만 쿠키 전송
        refreshCookie.setPath("/"); // 쿠키가 모든 경로에서 유효
        refreshCookie.setMaxAge((int) (jwtService.getRefreshTokenExpiration() / 1000)); // 쿠키 만료 시간 설정

        // 쿠키 추가
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return userInfo;
    }

    @GetMapping("/validate")
    @Operation(summary = "Access 토큰 검증", description = "Access 토큰 검증")
    public String validateToken(HttpServletRequest request) {
        Optional<Cookie> accessTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .findFirst();

        if (accessTokenCookie.isPresent()) {
            Claims claims = jwtService.validateToken(accessTokenCookie.get().getValue());
            if (claims != null) {
                return "Token is valid for user: " + claims.getSubject();
            } else {
                return "Invalid token";
            }
        } else {
            return "Access token not found";
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Access 토큰 갱신", description = "Refresh 토큰 이용해 Access 토큰 갱신")
    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst();

        logger.info("Refresh endpoint accessed");

        if (refreshTokenCookie.isPresent()) {
            try {
                Claims claims = jwtService.validateToken(refreshTokenCookie.get().getValue());
                String newAccessToken = jwtService.generateAccessToken(claims.getSubject());

                // 새로운 액세스 토큰을 쿠키로 설정
                Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
                newAccessCookie.setHttpOnly(true);
                newAccessCookie.setSecure(true);
                newAccessCookie.setPath("/");
                newAccessCookie.setMaxAge((int) (jwtService.getAccessTokenExpiration() / 1000));

                response.addCookie(newAccessCookie);

                // 로그 출력
                logger.info("New access token generated for user: {}", claims.getSubject());

                return ResponseEntity.ok("Access token refreshed successfully");
            } catch (JwtException e) {
                logger.error("Failed to validate refresh token", e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } else {
            logger.warn("Refresh token not found in request");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
        }
    }
}
