package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.PointDto;
import io.github.bigpig.back.exceptions.FetchDataException;
import io.github.bigpig.back.util.UrlBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricesServiceTest {

    @Mock
    private UrlBuilder urlBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private PricesService pricesService;

    private final String TICKER = "AAPL";
    private final String FUNCTION = "TIME_SERIES_DAILY";
    private final String URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=AAPL&apikey=demo";

    @BeforeEach
    void setUp() {
        pricesService = new PricesService(urlBuilder, restClient);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("getPrices should return list of PointDto when API call succeeds")
    void getPrices_Success() {
        // Arrange
        String jsonResponse = """
            {
                "Meta Data": {
                    "1. Information": "Daily Prices (open, high, low, close) and Volumes",
                    "2. Symbol": "AAPL",
                    "3. Last Refreshed": "2024-01-19",
                    "4. Output Size": "Compact",
                    "5. Time Zone": "US/Eastern"
                },
                "Time Series (Daily)": {
                    "2024-01-19": {
                        "1. open": "150.0000",
                        "2. high": "155.0000",
                        "3. low": "148.0000",
                        "4. close": "153.0000",
                        "5. volume": "1000000"
                    },
                    "2024-01-18": {
                        "1. open": "149.0000",
                        "2. high": "152.0000",
                        "3. low": "147.0000",
                        "4. close": "150.0000",
                        "5. volume": "950000"
                    }
                }
            }
            """;

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class))
                .thenReturn(new org.springframework.http.ResponseEntity<>(jsonResponse, org.springframework.http.HttpStatus.OK));

        // Expected results: avg_price = (high + low) / 2
        // 2024-01-19: (155.0 + 148.0) / 2 = 151.50
        // 2024-01-18: (152.0 + 147.0) / 2 = 149.50
        PointDto expectedPoint1 = new PointDto("2024-01-18", 149.50);
        PointDto expectedPoint2 = new PointDto("2024-01-19", 151.50);


        // Act
        List<PointDto> result = pricesService.getPrices(TICKER);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Check first point
        assertEquals(expectedPoint1.date(), result.get(0).date());
        assertEquals(expectedPoint1.price(), result.get(0).price(), 0.001);

        // Check second point
        assertEquals(expectedPoint2.date(), result.get(1).date());
        assertEquals(expectedPoint2.price(), result.get(1).price(), 0.001);

        // Verify interactions
        verify(urlBuilder).buildAlphaVintageUrl(TICKER, FUNCTION);
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(URL);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).toEntity(String.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("getPrices should throw FetchDataException when API call fails")
    void getPrices_ApiCallFails_ThrowsFetchDataException() {
        // Arrange
        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class))
                .thenThrow(new RuntimeException("Network error"));

        // Act & Assert
        FetchDataException exception = assertThrows(FetchDataException.class,
                () -> pricesService.getPrices(TICKER));

        assertEquals("Failed to fetch prices data for ticker: AAPL", exception.getMessage());

        verify(urlBuilder).buildAlphaVintageUrl(TICKER, FUNCTION);
        verify(responseSpec).toEntity(String.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("getPrices should throw FetchDataException when JSON parsing fails")
    void getPrices_InvalidJson_ThrowsFetchDataException() {
        // Arrange
        String invalidJson = "invalid json";

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class))
                .thenReturn(new org.springframework.http.ResponseEntity<>(invalidJson, org.springframework.http.HttpStatus.OK));

        // Act & Assert
        FetchDataException exception = assertThrows(FetchDataException.class,
                () -> pricesService.getPrices(TICKER));

        assertEquals("Failed to fetch prices data for ticker: AAPL", exception.getMessage());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("getPrices should throw FetchDataException when Time Series data is missing")
    void getPrices_MissingTimeSeries_ThrowsFetchDataException() {
        // Arrange
        String jsonWithoutTimeSeries = """
            {
                "Meta Data": {
                    "1. Information": "Daily Prices (open, high, low, close) and Volumes",
                    "2. Symbol": "AAPL",
                    "3. Last Refreshed": "2024-01-19"
                }
            }
            """;

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class))
                .thenReturn(new org.springframework.http.ResponseEntity<>(jsonWithoutTimeSeries, org.springframework.http.HttpStatus.OK));

        // Act & Assert
        FetchDataException exception = assertThrows(FetchDataException.class,
                () -> pricesService.getPrices(TICKER));

        assertEquals("Failed to fetch prices data for ticker: AAPL", exception.getMessage());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("getPrices should handle empty Time Series data")
    void getPrices_EmptyTimeSeries_ReturnsEmptyList() {
        // Arrange
        String jsonWithEmptyTimeSeries = """
            {
                "Meta Data": {
                    "1. Information": "Daily Prices (open, high, low, close) and Volumes",
                    "2. Symbol": "AAPL",
                    "3. Last Refreshed": "2024-01-19"
                },
                "Time Series (Daily)": {}
            }
            """;

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class))
                .thenReturn(new org.springframework.http.ResponseEntity<>(jsonWithEmptyTimeSeries, org.springframework.http.HttpStatus.OK));

        // Act
        List<PointDto> result = pricesService.getPrices(TICKER);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("getPrices should correctly calculate average price with rounding")
    void getPrices_CalculatesAveragePriceWithRounding() {
        // Arrange
        String jsonResponse = """
            {
                "Time Series (Daily)": {
                    "2024-01-19": {
                        "2. high": "155.1234",
                        "3. low": "148.5678"
                    }
                }
            }
            """;

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(String.class))
                .thenReturn(new org.springframework.http.ResponseEntity<>(jsonResponse, org.springframework.http.HttpStatus.OK));

        // Expected: (155.1234 + 148.5678) / 2 = 151.8456 → rounded to 151.85
        double expectedPrice = 151.85;

        // Act
        List<PointDto> result = pricesService.getPrices(TICKER);

        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedPrice, result.get(0).price(), 0.001);
        assertEquals("2024-01-19", result.get(0).date());
    }
}