package com.comp.tasks.security.jwt;

import com.comp.tasks.security.exceptions.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JWTUtil {
    JwtProperties jwtProperties;
    @NonFinal
    Key key;
    @NonFinal
    Key refreshKey;
    @NonFinal
    JwtParser jwtParser;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.refreshKey = jwtProperties.getRefreshSecret() != null ? Keys.hmacShaKeyFor(jwtProperties.getRefreshSecret().getBytes()) : null;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new AuthenticationException(e.getMessage());
        }
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        return doGenerateToken(claims, username);
    }

    public String generateRefreshToken(String fingerprint, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fingerprint", fingerprint);
        claims.put("username", username);
        return doGenerateRefreshToken(claims, fingerprint);
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        if (jwtProperties.getExpiration() == null) {
            throw new RuntimeException("you need to initialize the variable 'expiration' in config file");
        }
        long expirationTimeLong = Long.parseLong(jwtProperties.getExpiration()); //in second

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String fingerprint) {
        if (refreshKey == null) {
            throw new RuntimeException("you need to initialize the variable 'refreshSecret' in config file");
        }
        if (jwtProperties.getExpiration() == null) {
            throw new RuntimeException("you need to initialize the variable 'refreshExpiration' in config file");
        }
        long expirationTimeLong = Long.parseLong(jwtProperties.getRefreshExpiration()); //in second

        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(fingerprint)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
