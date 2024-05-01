package com.stock.realtime.rtstock.marketdata;

import com.stock.realtime.rtstock.price.PriceService;
import com.stock.realtime.rtstock.security.Security;
import com.stock.realtime.rtstock.security.SecurityRepository;
import com.stock.realtime.rtstock.security.SecurityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Component
public class MarketDataProvider {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PriceService priceService;
    private final SecurityRepository securityRepository;

    @Autowired
    public MarketDataProvider(ApplicationEventPublisher applicationEventPublisher, PriceService priceService,
                              SecurityRepository securityRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.priceService = priceService;
        this.securityRepository = securityRepository;
    }

    @Scheduled(initialDelay = 2, fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    @Transactional(readOnly = true)
    public void publishMarketData() {
        securityRepository.findAllByType(SecurityType.STOCK)
                .forEach(this::publishEvent);
    }

    private void publishEvent(Security security) {
        BigDecimal price = priceService.generatePrice(security);
        MarketData marketData = new MarketData(this, security.getTicker(), price);

        applicationEventPublisher.publishEvent(marketData);
    }
}
