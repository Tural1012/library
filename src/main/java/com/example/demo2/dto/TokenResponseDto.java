package com.example.demo2.dto;

public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {}
