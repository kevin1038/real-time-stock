package com.stock.realtime.rtstock.marketdata;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

public class MarketData extends ApplicationEvent {

    private String ticker;

    private BigDecimal price;

    public MarketData(Object source, String ticker, BigDecimal price) {
        super(source);
        this.ticker = ticker;
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
