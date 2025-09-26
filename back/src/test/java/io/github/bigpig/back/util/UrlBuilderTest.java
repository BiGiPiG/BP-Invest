package io.github.bigpig.back.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UrlBuilderTest {

    @InjectMocks
    private UrlBuilder urlBuilder = new UrlBuilder("demo");

    @Test
    void buildAlphaVintageUrlTest() {
        String expected = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=demo";
        String actual = urlBuilder.buildAlphaVintageUrl("IBM", "OVERVIEW");
        assertEquals(expected, actual);
    }

    @Test
    void buildAnalyseUrlTest() {
        String expected = "http://ai-analysis:9000/analyze";
        String actual = urlBuilder.buildAnalyseUrl();
        assertEquals(expected, actual);
    }
}
