package com.example.demo2.service;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access-exp-time}")
    private long ACCESS_EXPIRATION;
    @Value("${jwt.refresh-exp-time}")
    private long REFRESH_EXPIRATION;


    private SecretKey key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    private String buildToken(UserDetails user, long exp, String type) {

        List<String> auth = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuer("letcode")
                .claim("auth", auth)
                .claim("type", type) // access / refresh
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + exp))
                .signWith(key())
                .compact();
    }

    public String generateAccessToken(UserDetails user) {
        return buildToken(user, ACCESS_EXPIRATION, "access");
    }

    public String generateRefreshToken(UserDetails user) {
        return buildToken(user, REFRESH_EXPIRATION, "refresh");
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
