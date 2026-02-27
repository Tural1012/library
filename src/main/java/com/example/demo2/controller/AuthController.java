package com.example.demo2.controller;


import com.example.demo2.dto.LoginRequestDto;
import com.example.demo2.dto.TokenResponseDto;
import com.example.demo2.service.JwtService;
import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager manager;
    private final JwtService jwt;
    private final UserDetailsService userDetailsService;



    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto req) {
        Authentication auth =
                manager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        UserDetails user = (UserDetails) auth.getPrincipal();
        return new TokenResponseDto(jwt.generateAccessToken(user), jwt.generateRefreshToken(user));
    }

    @PostMapping("/refresh")
    public TokenResponseDto refresh(@RequestHeader("Authorization") String authToken) {
        Claims claims = jwt.parse(authToken.substring(7));
        if (!"refresh".equals(claims.get("type"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid refresh token");
        }
        String username = claims.getSubject();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return new TokenResponseDto(jwt.generateAccessToken(user), jwt.generateRefreshToken(user));
    }

}
