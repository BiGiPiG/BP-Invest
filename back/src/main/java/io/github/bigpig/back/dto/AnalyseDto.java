package io.github.bigpig.back.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AnalyseDto(
    @JsonProperty("overallAssessment") String overallAssessment,
    @JsonProperty("rating") String rating,
    @JsonProperty("pros") List<String> pros,
    @JsonProperty("cons" ) List<String> cons
) {
}
