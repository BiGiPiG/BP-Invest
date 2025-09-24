package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.AnalyseDto;
import io.github.bigpig.back.dto.PointDto;
import io.github.bigpig.back.dto.ShareDto;
import io.github.bigpig.back.services.AnalyseService;
import io.github.bigpig.back.services.PricesService;
import io.github.bigpig.back.services.ShareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ShareControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShareService shareService;

    @Mock
    private PricesService pointService;

    @Mock
    private AnalyseService analyseService;

    @InjectMocks
    private ShareController shareController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(shareController).build();
    }

    @Test
    void getMainMultipliersTest() throws Exception {
        // Arrange
        String ticker = "AAPL";
        ShareDto mockShareDto = new ShareDto("AAPL", "Apple Inc.", "20",
                "20", "20", "20", "20");
        when(shareService.getMainInfo(ticker)).thenReturn(mockShareDto);

        // Act & Assert
        mockMvc.perform(get("/bp-invest/api/v1/main-info")
                        .param("ticker", ticker)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(shareService, times(1)).getMainInfo(ticker);
        verifyNoMoreInteractions(shareService);
    }

    @Test
    void getCharInfoTest() throws Exception {
        // Arrange
        String ticker = "TSLA";
        List<PointDto> mockPoints = List.of(new PointDto("10.09.2025", 150.5),
                new PointDto("11.09.2025", 151.5));
        when(pointService.getPrices(ticker)).thenReturn(mockPoints);

        // Act & Assert
        mockMvc.perform(get("/bp-invest/api/v1/chart-info")
                        .param("ticker", ticker)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(pointService, times(1)).getPrices(ticker);
        verifyNoMoreInteractions(pointService);
    }

    @Test
    void getAiAnalyseTest() throws Exception {
        // Arrange
        String ticker = "GOOGL";
        AnalyseDto mockAnalyseDto = new AnalyseDto(
                "Apple inc. has a strong market position and solid financials. making it an attractive investment.",
                "7/10 - moderately attractive",
                List.of("Pros1", "Pros2"), List.of("Cons1", "Cons2"));
        when(analyseService.getAnalyse(ticker)).thenReturn(mockAnalyseDto);

        // Act & Assert
        mockMvc.perform(get("/bp-invest/api/v1/ai-analyse")
                        .param("ticker", ticker)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists());

        verify(analyseService, times(1)).getAnalyse(ticker);
        verifyNoMoreInteractions(analyseService);
    }
}