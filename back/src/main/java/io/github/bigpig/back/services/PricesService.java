package io.github.bigpig.back.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bigpig.back.dto.PointDto;
import io.github.bigpig.back.exceptions.FetchDataException;
import io.github.bigpig.back.util.UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PricesService {

    private final UrlBuilder urlBuilder;
    private final RestTemplate restTemplate;

    public List<PointDto> getPrices(String ticker) {
        try {
            String function = "TIME_SERIES_DAILY";
            String url = urlBuilder.buildAlphaVintageUrl(ticker, function);
            String json = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);

            JsonNode timeSeries = root.get("Time Series (Daily)");

            List<PointDto> points = new ArrayList<>();

            Iterator<Map.Entry<String, JsonNode>> it = timeSeries.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                String date = entry.getKey();
                JsonNode dailyData = entry.getValue();

                double high = dailyData.get("2. high").asDouble();
                double low = dailyData.get("3. low").asDouble();

                double avg_price = (high + low) / 2.0;

                BigDecimal bd = new BigDecimal(avg_price);
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                Double price = bd.doubleValue();

                points.add(new PointDto(date, price));

            }
            return points;
        } catch (Exception e) {
            throw new FetchDataException(String.format("Failed to fetch prices data for ticker: %s", ticker));
        }
    }
}
