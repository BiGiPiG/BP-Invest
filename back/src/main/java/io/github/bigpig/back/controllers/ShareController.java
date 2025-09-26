package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.AnalyseDto;
import io.github.bigpig.back.dto.ExceptionDto;
import io.github.bigpig.back.dto.PointDto;
import io.github.bigpig.back.services.AnalyseService;
import io.github.bigpig.back.services.PricesService;
import io.github.bigpig.back.services.ShareService;
import io.github.bigpig.back.dto.ShareDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bp-invest/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class ShareController {

    private final ShareService shareService;
    private final PricesService pointService;
    private final AnalyseService analyseService;

    @Operation(
            summary = "Get method for getting the main multipliers",
            description = "Retrieves fundamental financial multipliers and key metrics for the specified stock ticker",
            parameters = {
                    @Parameter(
                            name = "ticker",
                            description = "Stock ticker symbol (e.g., AAPL, GOOGL, MSFT)",
                            example = "AAPL",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved main info",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    	"ticker": "AAPL",
                                                    	"name": "Apple Inc.",
                                                    	"Marcket Cap": "2.4T",
                                                    	"P/E": "28.7",
                                                    	"P/B": "40.1",
                                                    	"P/S": "7.5",
                                                    	"EPS": "6.21"
                                                    }
                                             """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionDto.class))
                    )
            }
    )
    @GetMapping("/main-info")
    public ShareDto getMainMultipliers(@RequestParam String ticker) {
        log.info("getMainMultipliers - {}", ticker);
        return shareService.getMainInfo(ticker);
    }

    @Operation(
            summary = "Get method for getting prices",
            description = "Retrieves historical price data for chart visualization",
            parameters = {
                    @Parameter(
                            name = "ticker",
                            description = "Stock ticker symbol",
                            example = "AAPL",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved price data",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                    [
                      {
                        "date": "2024-01-15",
                        "price": 185.20
                      },
                      {
                        "date": "2024-01-16",
                        "price": 186.50
                      }
                    ]
                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionDto.class))
                    )
            }
    )
    @GetMapping("/chart-info")
    public List<PointDto> getCharInfo(@RequestParam String ticker) {
        log.info("getCharInfo - {}", ticker);
        return pointService.getPrices(ticker);
    }

    @Operation(
            summary = "Get method for getting AI analysis",
            description = """
                Retrieves AI-powered technical and fundamental analysis.

                **Features included:**
                - Technical indicators analysis
                - Sentiment analysis
                - Price predictions
                - Risk assessment
            """,
            parameters = {
                    @Parameter(
                            name = "ticker",
                            description = "Stock ticker symbol supported by our AI model",
                            example = "AAPL",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully received AI analysis data",
                            content =  @Content(
                                    mediaType = "Application/Json",
                                    examples = @ExampleObject(
                                            value = """
                                                {
                                                    "overallAssessment": "Apple remains a dominant force in the tech sector with strong revenue streams, high brand loyalty, and a resilient ecosystem. While growth in hardware may be moderating, services and emerging product areas offer long-term potential. Near-term valuation is rich but backed by a robust balance sheet and cash flow.",
                                                    "rating": "8/10 – Strong long-term hold",
                                                    "pros": [
                                                    "Strong brand loyalty",
                                                    "Diversified revenue streams",
                                                    "Robust financial position"
                                                    ],
                                                    "cons": [
                                                    "High valuation",
                                                    "Hardware growth slowdown",
                                                    "Dependence on iPhone sales"
                                                    ]
                                                }
                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ExceptionDto.class))
                    )
            }
    )
    @GetMapping("/ai-analyse")
    public AnalyseDto getAiAnalyse(@RequestParam String ticker) {
        log.info("getAiAnalyse - {}", ticker);
        return analyseService.getAnalyse(ticker);
    }
}
