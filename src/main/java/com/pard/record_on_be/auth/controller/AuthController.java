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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "구글 로그인으로 받은 정보 넘겨주세요~")
    public Map<String, Object> googleLogin(@RequestBody UserLoginRequestDTO userLoginRequestDTO, HttpServletResponse response) {
        Map<String, Object> userInfo = authService.saveOrUpdateUser(userLoginRequestDTO);

        String accessToken = jwtService.generateAccessToken(userLoginRequestDTO.getEmail());
        String refreshToken = jwtService.generateRefreshToken(userLoginRequestDTO.getEmail());

        addCookie(response, "access_token", accessToken, (int) (jwtService.getAccessTokenExpiration() / 1000));

        addCookie(response, "refresh_token", refreshToken, (int) (jwtService.getRefreshTokenExpiration() / 1000));

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

        if (refreshTokenCookie.isPresent()) {
            try {
                Claims claims = jwtService.validateToken(refreshTokenCookie.get().getValue());
                String newAccessToken = jwtService.generateAccessToken(claims.getSubject());

                addCookie(response, "access_token", newAccessToken, (int) (jwtService.getAccessTokenExpiration() / 1000));

                return ResponseEntity.ok("Access token refreshed successfully");
            } catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found");
        }
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .sameSite("None")
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}