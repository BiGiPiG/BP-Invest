package io.github.bigpig.back.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ShareDto(
        @JsonProperty("ticker") @JsonAlias("Symbol") String ticker,
        @JsonProperty("name") @JsonAlias("Name") String name,
        @JsonProperty("market_cap") @JsonAlias("MarketCapitalization") String marketCap,
        @JsonProperty("EPS") @JsonAlias("EPS") String eps,
        @JsonProperty("P/E") @JsonAlias("PERatio") String priceToEarnings,
        @JsonProperty("P/S") @JsonAlias("PriceToSalesRatioTTM") String priceToSales,
        @JsonProperty("P/B") @JsonAlias("PriceToBookRatio") String priceToBook
) {}