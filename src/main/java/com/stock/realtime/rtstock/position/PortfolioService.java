package com.stock.realtime.rtstock.position;

import com.stock.realtime.rtstock.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PortfolioService {

    private final MarketDataService marketDataService;
    private final List<Position> positions;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<String, BigDecimal> marketPriceUpdate = new ConcurrentHashMap<>();

    @Autowired
    public PortfolioService(PositionCSVReader positionCSVReader, MarketDataService marketDataService)
            throws IOException {
        this.positions = positionCSVReader.readCsv();
        this.marketDataService = marketDataService;
    }

    public void pushMarketData(String ticker, BigDecimal price) {
        marketPriceUpdate.put(ticker, price);
    }

    @Scheduled(initialDelay = 5, fixedRate = 2, timeUnit = TimeUnit.SECONDS)
    private void printPortfolio() {
        StringBuilder portfolio = new StringBuilder();
        portfolio.append(String.format("\n## %d Market Data Update\n", counter.incrementAndGet()));
        marketPriceUpdate.forEach((ticker, price) ->
                portfolio.append(String.format("%s change to %.2f\n", ticker, price)));

        portfolio.append("\n# Portfolio\n");
        portfolio.append(String.format("%-25s %15s %15s %15s\n", "symbol", "price", "qty", "value"));
        AtomicReference<BigDecimal> totalValue = new AtomicReference<>(BigDecimal.ZERO);

        positions.forEach(position -> {
            BigDecimal price = marketDataService.getMarketPrice(position.getSymbol());
            BigDecimal value = price.multiply(BigDecimal.valueOf(position.getPositionSize()))
                    .setScale(2, RoundingMode.HALF_UP);

            totalValue.set(totalValue.get().add(value));
            portfolio.append(String.format("%-25s %,15.2f %,15d %,15.2f\n",
                    position.getSymbol(), price, position.getPositionSize(), value));
        });

        portfolio.append(String.format("\n%-25s %,47.2f\n", "#Total portfolio", totalValue.get()));

        System.out.println(portfolio);
        marketPriceUpdate.clear();
    }
}
