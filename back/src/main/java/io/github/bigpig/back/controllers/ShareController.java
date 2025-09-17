package io.github.bigpig.back.controllers;

import io.github.bigpig.back.dto.AnalyseDto;
import io.github.bigpig.back.dto.PointDto;
import io.github.bigpig.back.services.AnalyseService;
import io.github.bigpig.back.services.PricesService;
import io.github.bigpig.back.services.ShareService;
import io.github.bigpig.back.dto.ShareDto;
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

    @GetMapping("/main-info")
    public ShareDto getMainMultipliers(@RequestParam String ticker) {
        log.info("getMainMultipliers - {}", ticker);
        return shareService.getMainInfo(ticker);
    }

    @GetMapping("/chart-info")
    public List<PointDto> getCharInfo(@RequestParam String ticker) {
        log.info("getCharInfo - {}", ticker);
        return pointService.getPrices(ticker);
    }

    @GetMapping("/ai-analyse")
    public AnalyseDto getAiAnalyse(@RequestParam String ticker) {
        log.info("getAiAnalyse - {}", ticker);
        return analyseService.getAnalyse(ticker);
    }
}
