package com.stock.realtime.rtstock.marketdata;

import com.stock.realtime.rtstock.price.PriceService;
import com.stock.realtime.rtstock.security.Security;
import com.stock.realtime.rtstock.security.SecurityRepository;
import com.stock.realtime.rtstock.security.SecurityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class MarketDataProvider {

    private static final long MIN_DELAY_MILLISECONDS = 500;
    private static final long MAX_DELAY_MILLISECONDS = 2000;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final PriceService priceService;
    private final SecurityRepository securityRepository;
    private final TaskScheduler taskScheduler;

    @Autowired
    public MarketDataProvider(ApplicationEventPublisher applicationEventPublisher, PriceService priceService,
                              SecurityRepository securityRepository, TaskScheduler taskScheduler) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.priceService = priceService;
        this.securityRepository = securityRepository;
        this.taskScheduler = taskScheduler;
    }

    @PostConstruct
    private void init() {
        scheduleTask();
    }

    private void publishAllStock(long discreteMilliseconds) {
        scheduleTask();

        securityRepository.findAllByType(SecurityType.STOCK)
                .forEach(security -> publishEvent(security, discreteMilliseconds));
    }

    private void publishEvent(Security security, long discreteMilliseconds) {
        BigDecimal price = priceService.generateStockPrice(security, discreteMilliseconds);
        MarketData marketData = new MarketData(this, security.getTicker(), price);

        applicationEventPublisher.publishEvent(marketData);
    }

    private void scheduleTask() {
        long discreteMilliseconds = generateDiscreteMilliseconds();
        Instant startTime = Instant.now().plusMillis(discreteMilliseconds);

        taskScheduler.schedule(() -> publishAllStock(discreteMilliseconds), startTime);
    }

    private long generateDiscreteMilliseconds() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return MIN_DELAY_MILLISECONDS + random.nextLong(MAX_DELAY_MILLISECONDS - MIN_DELAY_MILLISECONDS);
    }
}
