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

    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

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
        // JWT 토큰 발급
        String jwtToken = jwtUtil.generateToken(email);

        // JWT 토큰을 쿠키로 설정
        Cookie cookie = new Cookie("jwt", jwtToken);
        cookie.setHttpOnly(true); // 클라이언트 측에서 자바스크립트로 접근 불가
        cookie.setSecure(true); // HTTPS에서만 쿠키 전송
        cookie.setPath("/"); // 쿠키가 모든 경로에서 유효
        cookie.setMaxAge((int) EXPIRATION_TIME / 1000); // 쿠키 만료 시간 설정

        response.addCookie(cookie);

        return "User authenticated successfully";
    }


}
