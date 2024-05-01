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
    public void onApplicationEvent(MarketData marketData) {
        BigDecimal previousPrice = marketDataService.getMarketPrice(marketData.getTicker());
        BigDecimal currentPrice = marketData.getPrice();

        if (currentPrice.equals(previousPrice)) {
            return;
        }

        securityRepository.findAllByUnderlyingStock(marketData.getTicker())
                .forEach(security -> {
                    BigDecimal price = priceService.generatePrice(security);
                    marketDataService.pushMarketData(new MarketData(this, security.getTicker(), price));
                });

        marketDataService.pushMarketData(marketData);
        portfolioService.pushMarketData(marketData);
    }
}
