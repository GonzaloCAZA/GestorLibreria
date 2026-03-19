package com.example.libreria.security;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtService(JwtProperties jwtProperties, JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtProperties = jwtProperties;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(UserDetails userDetails) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusMillis(jwtProperties.getExpiration());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("roles", userDetails.getAuthorities().stream().map(Object::toString).toList())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public ResponseCookie buildAuthCookie(String token) {
        return ResponseCookie.from(jwtProperties.getCookieName(), token)
                .httpOnly(true)
                .secure(jwtProperties.isSecureCookie())
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofMillis(jwtProperties.getExpiration()))
                .build();
    }

    public ResponseCookie buildLogoutCookie() {
        return ResponseCookie.from(jwtProperties.getCookieName(), "")
                .httpOnly(true)
                .secure(jwtProperties.isSecureCookie())
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
    }

    public String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (jwtProperties.getCookieName().equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private boolean isTokenExpired(String token) {
        Instant expiresAt = decodeToken(token).getExpiresAt();
        return expiresAt == null || expiresAt.isBefore(Instant.now());
    }

    private Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

}
