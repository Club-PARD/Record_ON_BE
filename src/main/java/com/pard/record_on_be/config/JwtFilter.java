package com.pard.record_on_be.config;

import com.pard.record_on_be.auth.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    public JwtFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromCookies(request);

        if (token != null) {
            try {
                Claims claims = jwtService.validateToken(token);
                if (claims != null) {
                    // SecurityContextHolder 에 인증 정보 설정
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            claims.getSubject(), null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 유효한 토큰일 경우, 요청을 계속 처리
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Expired token");
                return;
            } catch (UnsupportedJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unsupported token");
                return;
            } catch (MalformedJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Malformed token");
                return;
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else if (!isPublicEndpoint(request)) {
            // 토큰이 없고, public endpoint 가 아닌 경우
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Access token not found");
            return;
        }

        // public endpoint 일 경우
        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            logger.debug("Cookies present in request");
            Optional<Cookie> cookie = Arrays.stream(request.getCookies())
                    .filter(c -> "access_token".equals(c.getName()))
                    .findFirst();
            if (cookie.isPresent()) {
                logger.debug("Access token cookie found");
                return cookie.get().getValue();
            } else {
                logger.debug("Access token cookie not found");
            }
        } else {
            logger.debug("No cookies present in request");
        }
        return null;
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/health") ||
                path.startsWith("/api/auth/refresh");
    }
}