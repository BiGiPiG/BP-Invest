package io.github.bigpig.back.dto;

public record RegistrationUserDto(
        String username,
        String password,
        String email
) {}
