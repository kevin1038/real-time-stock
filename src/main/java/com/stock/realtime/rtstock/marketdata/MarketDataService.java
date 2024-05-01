package com.stock.realtime.rtstock.marketdata;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketDataService {

    private final Map<String, BigDecimal> marketPrice = new ConcurrentHashMap<String, BigDecimal>() {{
        put("AAPL", BigDecimal.valueOf(110));
        put("AAPL-OCT-2024-110-C", BigDecimal.valueOf(5.55));
        put("AAPL-OCT-2024-110-P", BigDecimal.valueOf(0.55));
        put("TELSA", BigDecimal.valueOf(450));
        put("TELSA-NOV-2024-400-C", BigDecimal.valueOf(27.25));
        put("TELSA-DEC-2024-400-P", BigDecimal.valueOf(6.35));
    }};

    public void pushMarketData(MarketData marketData) {
        marketPrice.put(marketData.getTicker(), marketData.getPrice());
    }

    public BigDecimal getMarketPrice(String ticker) {
        return marketPrice.get(ticker);
    }
}
