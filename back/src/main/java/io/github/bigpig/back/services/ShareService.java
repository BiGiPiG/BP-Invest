package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.ShareDto;
import io.github.bigpig.back.exceptions.FetchDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class ShareService {

    @Value("${alpha-vintage.apiKey}")
    private String apiKey;
    private final String function = "OVERVIEW";
    private final RestTemplate restTemplate;

    public ShareDto getMainInfo(String ticker) {
        String url = buildUrl(ticker);
        ShareDto info = null;
        try {
            info = restTemplate.getForObject(url, ShareDto.class);
        } catch (Exception ex) {
            throw new FetchDataException(String.format("Failed to fetch share data for ticker: %s", ticker));
        }
        return info;
    }

    public String buildUrl(String ticker) {
        return UriComponentsBuilder.fromUriString("https://www.alphavantage.co/query")
                .queryParam("function", function)
                .queryParam("symbol", ticker)
                .queryParam("apikey", apiKey)
                .toUriString();
    }
}
