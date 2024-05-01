package com.stock.realtime.rtstock.marketdata;

import com.stock.realtime.rtstock.position.PortfolioService;
import com.stock.realtime.rtstock.price.PriceService;
import com.stock.realtime.rtstock.security.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MarketDataListener {

    private final MarketDataService marketDataService;
    private final PortfolioService portfolioService;
    private final SecurityRepository securityRepository;
    private final PriceService priceService;

    @Autowired
    public MarketDataListener(MarketDataService marketDataService, PortfolioService portfolioService,
                              SecurityRepository securityRepository, PriceService priceService) {
        this.marketDataService = marketDataService;
        this.portfolioService = portfolioService;
        this.securityRepository = securityRepository;
        this.priceService = priceService;
    }

    @EventListener
    private void onApplicationEvent(MarketData marketData) {
        String ticker = marketData.getTicker();
        BigDecimal previousPrice = marketDataService.getMarketPrice(ticker);
        BigDecimal currentPrice = marketData.getPrice();

        if (currentPrice.equals(previousPrice)) {
            return;
        }

        securityRepository.findAllByUnderlyingStock(ticker)
                .forEach(security -> {
                    BigDecimal price = priceService.generateOptionPrice(security);
                    marketDataService.pushMarketData(security.getTicker(), price);
                });

        marketDataService.pushMarketData(ticker, currentPrice);
        portfolioService.pushMarketData(ticker, currentPrice);
    }
}
