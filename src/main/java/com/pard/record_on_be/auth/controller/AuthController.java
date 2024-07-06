package com.pard.record_on_be.auth.controller;

import com.pard.record_on_be.auth.service.AuthService;
import com.pard.record_on_be.util.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;


import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    public AuthController(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "구글 로그인으로 받은 정보 넘겨주세요~")
    public Map<String, Object> googleLogin(@RequestBody Map<String, Object> userData, HttpServletResponse response) {
        String email = (String) userData.get("email");
        String name = (String) userData.get("name");
        String imageUrl = (String) userData.get("imageUrl");

        Map<String, Object> userInfo = authService.saveOrUpdateUser(email, name, imageUrl);

        // 액세스 토큰 및 리프레시 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // 액세스 토큰을 쿠키로 설정
        Cookie accessCookie = new Cookie("access_token", accessToken);
        accessCookie.setHttpOnly(true); // 클라이언트 측에서 자바스크립트로 접근 불가
        accessCookie.setSecure(true); // HTTPS 에서만 쿠키 전송
        accessCookie.setPath("/"); // 쿠키가 모든 경로에서 유효
        accessCookie.setMaxAge((int) (JwtUtil.ACCESS_EXPIRATION_TIME / 1000)); // 쿠키 만료 시간 설정

        // 리프레시 토큰을 쿠키로 설정
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true); // 클라이언트 측에서 자바스크립트로 접근 불가
        refreshCookie.setSecure(true); // HTTPS 에서만 쿠키 전송
        refreshCookie.setPath("/"); // 쿠키가 모든 경로에서 유효
        refreshCookie.setMaxAge((int) (JwtUtil.REFRESH_EXPIRATION_TIME / 1000)); // 쿠키 만료 시간 설정

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
            Claims claims = jwtUtil.validateToken(accessTokenCookie.get().getValue());
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
    public String refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .findFirst();

        if (refreshTokenCookie.isPresent()) {
            try {
                Claims claims = jwtUtil.validateToken(refreshTokenCookie.get().getValue());
                String newAccessToken = jwtUtil.generateAccessToken(claims.getSubject());

                // 새로운 액세스 토큰을 쿠키로 설정
                Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
                newAccessCookie.setHttpOnly(true);
                newAccessCookie.setSecure(true);
                newAccessCookie.setPath("/");
                newAccessCookie.setMaxAge((int) (JwtUtil.ACCESS_EXPIRATION_TIME / 1000));

                response.addCookie(newAccessCookie);

                return "Access token refreshed successfully";
            } catch (Exception e) {
                return "Invalid refresh token";
            }
        } else {
            return "Refresh token not found";
        }
    }
}
