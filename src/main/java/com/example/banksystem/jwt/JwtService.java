package com.example.banksystem.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {

    @Value("${security.jwt.key}")
    private String key;

    public String generateToken(String username, Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(jwtKey())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)))
                .compact();
    }


    public Claims claims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key jwtKey() {
        return Keys.hmacShaKeyFor(key.getBytes());
    }


}
