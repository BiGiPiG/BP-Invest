package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.ShareDto;
import io.github.bigpig.back.exceptions.FetchDataException;
import io.github.bigpig.back.util.UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ShareService {

    private final UrlBuilder urlBuilder;
    private final RestClient restClient;

    public ShareDto getMainInfo(String ticker) {
        String function = "OVERVIEW";
        String url = urlBuilder.buildAlphaVintageUrl(ticker, function);
        ShareDto info;
        try {
            info = restClient
                .get()
                .uri(url)
                .retrieve()
                .toEntity(ShareDto.class)
                .getBody();
        } catch (Exception ex) {
            throw new FetchDataException(String.format("Failed to fetch share data for ticker: %s", ticker));
        }
        return info;
    }
}
