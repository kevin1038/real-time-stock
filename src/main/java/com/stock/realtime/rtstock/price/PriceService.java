package com.stock.realtime.rtstock.price;

import com.stock.realtime.rtstock.marketdata.MarketDataService;
import com.stock.realtime.rtstock.security.Security;
import com.stock.realtime.rtstock.security.SecurityRepository;
import com.stock.realtime.rtstock.security.SecurityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Random;

import static com.stock.realtime.rtstock.price.PriceUtil.calculateCumulativeProbability;

@Service
public class PriceService {

    private static final double RISK_FREE_INTEREST_RATE = 0.02;

    private final SecurityRepository securityRepository;
    private final MarketDataService marketDataService;

    @Autowired
    public PriceService(SecurityRepository securityRepository, MarketDataService marketDataService) {
        this.securityRepository = securityRepository;
        this.marketDataService = marketDataService;
    }

    @EventListener
    private void init(ContextRefreshedEvent event) {
        securityRepository.findAllByType(SecurityType.STOCK)
                .forEach(security -> marketDataService.pushMarketData(security.getTicker(), BigDecimal.valueOf(security.getInitPrice())));

        securityRepository.findAllByTypeNot(SecurityType.STOCK)
                .forEach(security -> marketDataService.pushMarketData(security.getTicker(), generateOptionPrice(security)));
    }

    public BigDecimal generateStockPrice(Security security, long discreteMilliseconds) {
        BigDecimal currentPrice = marketDataService.getMarketPrice(security.getTicker());

        Random random = new Random();
        double t = discreteMilliseconds / 1000.0;
        double mu = security.getExpectedReturn();
        double sigma = security.getAnnualizedSD();
        double epsilon = random.nextGaussian();

        double deltaS = mu * (t / 7257600) + sigma * epsilon * Math.sqrt(t / 7257600);

        BigDecimal exp = BigDecimal.valueOf(Math.exp(deltaS));
        return currentPrice.multiply(exp).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal generateOptionPrice(Security security) {
        Security underlyingStock = securityRepository.findById(security.getUnderlyingStock())
                .orElseThrow(NoSuchElementException::new);

        double sigma = underlyingStock.getAnnualizedSD();
        double s = marketDataService.getMarketPrice(security.getUnderlyingStock()).doubleValue();
        double k = security.getStrike();
        double r = RISK_FREE_INTEREST_RATE;
        double t = ChronoUnit.DAYS.between(LocalDate.now(), security.getMaturityDate()) / 365.0;

        double d1 = (Math.log(s / k) + (r + (Math.pow(sigma, 2) / 2)) * t) / (sigma * Math.sqrt(t));
        double d2 = d1 - sigma * Math.sqrt(t);
        double price = security.getType() == SecurityType.PUT
                ? k * Math.exp(-r * t) * calculateCumulativeProbability(-d2) - s * calculateCumulativeProbability(-d1)
                : s * calculateCumulativeProbability(d1) - k * Math.exp(-r * t) * calculateCumulativeProbability(d2);

        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }
}
