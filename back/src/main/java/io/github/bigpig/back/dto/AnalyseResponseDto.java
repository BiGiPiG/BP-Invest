package io.github.bigpig.back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnalyseResponseDto (
    @JsonProperty("analysis") String analysis,
    @JsonProperty("symbol") String symbol
) {}
