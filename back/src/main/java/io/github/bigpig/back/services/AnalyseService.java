package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.AnalyseDto;
import io.github.bigpig.back.dto.AnalyseResponseDto;
import io.github.bigpig.back.exceptions.FetchDataException;
import io.github.bigpig.back.util.AnalysisParser;
import io.github.bigpig.back.util.UrlBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AnalyseService {

    private final RestClient restClient;
    private final UrlBuilder urlBuilder;
    private final AnalysisParser analysisParser;

    public AnalyseDto getAnalyse(String ticker) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("symbol", ticker);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        AnalyseDto analyseRes = null;
        try {
            AnalyseResponseDto response = restClient.post()
                .uri(urlBuilder.buildAnalyseUrl())
                .body(request)
                .retrieve()
                .toEntity(AnalyseResponseDto.class)
                .getBody();

            if (response != null) {
                analyseRes = analysisParser.parseAnalyse(response.analysis());
            } else {
                throw new FetchDataException(String.format("Failed to fetch analyse: %s", ticker));
            }
        } catch(IndexOutOfBoundsException ex) {
            getAnalyse(ticker);
        }
        return analyseRes;
    }
}
