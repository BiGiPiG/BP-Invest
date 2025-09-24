package io.github.bigpig.back.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UrlBuilder {

    @Value("${alpha-vintage.apiKey}")
    private String apiKey;

    public String buildAlphaVintageUrl(String ticker, String function) {
        return UriComponentsBuilder.fromUriString("https://www.alphavantage.co/query")
                .queryParam("function", function)
                .queryParam("symbol", ticker)
                .queryParam("apikey", apiKey)
                .toUriString();
    }

    public String buildAnalyseUrl() {
        return UriComponentsBuilder.fromUriString("http://ai-analysis:9000/analyze")
                .toUriString();
    }
}
