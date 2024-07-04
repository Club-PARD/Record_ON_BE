package com.pard.record_on_be.auth;

import com.pard.record_on_be.util.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test successful";
    }

    @PostMapping("/google")
    public String googleLogin(@RequestBody Map<String, Object> userData, HttpServletResponse response) {
        String email = (String) userData.get("email");
        String name = (String) userData.get("name");
        String imageUrl = (String) userData.get("imageUrl");

        // 여기서 사용자 정보를 처리 (예: DB에 저장하거나 업데이트)
        // ...

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

        return "User authenticated successfully";
    }


}
