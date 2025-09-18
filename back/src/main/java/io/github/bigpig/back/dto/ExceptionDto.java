package io.github.bigpig.back.dto;

import java.time.Instant;

public record ExceptionDto(
        String message,
        String errorCode
) {}
