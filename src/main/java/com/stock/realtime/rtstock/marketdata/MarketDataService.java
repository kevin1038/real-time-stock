package com.stock.realtime.rtstock.marketdata;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketDataService {

    private final Map<String, BigDecimal> marketPrice = new ConcurrentHashMap<>();

    public void pushMarketData(String ticker, BigDecimal price) {
        marketPrice.put(ticker, price);
    }

    public BigDecimal getMarketPrice(String ticker) {
        return marketPrice.get(ticker);
    }
}
