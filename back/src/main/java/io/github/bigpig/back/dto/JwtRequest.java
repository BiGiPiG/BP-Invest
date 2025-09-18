package io.github.bigpig.back.dto;

public record JwtRequest(
    String username,
    String password
) {}