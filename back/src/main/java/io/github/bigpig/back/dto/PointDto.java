package io.github.bigpig.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PointDto(
    @JsonProperty("day") String date,
    @JsonProperty("val") Double price
) {}
