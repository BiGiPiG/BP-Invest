package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.AnalyseDto;
import io.github.bigpig.back.dto.AnalyseResponseDto;
import io.github.bigpig.back.util.AnalysisParser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AnalyseService {

    RestTemplate restTemplate;
    private final AnalysisParser analysisParser;

    public AnalyseDto getAnalyse(String ticker) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("symbol", ticker);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        String url = buildUrl();
        AnalyseResponseDto response = restTemplate.postForObject(url, request, AnalyseResponseDto.class);
        assert response != null;
        return analysisParser.parseAnalyse(response.analysis());
    }

    public String buildUrl() {
        return UriComponentsBuilder.fromUriString("http://ai-analysis:9000/analyze")
                .toUriString();
    }
}
