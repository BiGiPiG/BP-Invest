package io.github.bigpig.back.services;

import io.github.bigpig.back.dto.ShareDto;
import io.github.bigpig.back.exceptions.FetchDataException;
import io.github.bigpig.back.util.UrlBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShareServiceTest {

    @Mock
    private UrlBuilder urlBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ShareService shareService;

    private static final String TICKER = "AAPL";
    private static final String FUNCTION = "OVERVIEW";
    private static final String URL = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=AAPL&apikey=demo";

    @Test
    @DisplayName("getMainInfo should return ShareDto when API call succeeds")
    void getMainInfo_Success() {
        // Arrange
        ShareDto expectedDto = new ShareDto(
                TICKER, "Apple Inc.", "2,4T", "20", "20", "20", "20"
        );

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(URL)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toEntity(ShareDto.class)).thenReturn(
                new ResponseEntity<>(expectedDto, HttpStatus.OK)
        );

        // Act
        ShareDto actualDto = shareService.getMainInfo(TICKER);

        // Assert
        assertNotNull(actualDto);
        assertEquals(expectedDto, actualDto);
        verify(urlBuilder).buildAlphaVintageUrl(TICKER, FUNCTION);
    }


    @Test
    @DisplayName("getMainInfo should throw FetchDataException when API call fails")
    void getMainInfo_Failure() {
        // Arrange
        ShareDto expectedDto = new ShareDto(
                TICKER, "Apple Inc.", "2,4T", "20", "20", "20", "20"
        );


        Mockito.when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);

        when(urlBuilder.buildAlphaVintageUrl(TICKER, FUNCTION)).thenReturn(URL);
        when(restClient.get()).thenThrow(new RuntimeException("Test Exception"));
        // Act & Assert
        FetchDataException exception = assertThrows(
                FetchDataException.class,
                () -> shareService.getMainInfo(TICKER)
        );

        assertEquals("Failed to fetch share data for ticker: AAPL", exception.getMessage());

        verify(urlBuilder).buildAlphaVintageUrl(TICKER, FUNCTION);
    }
}