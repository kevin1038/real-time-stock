package com.stock.realtime.rtstock.security;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Security {

    @Column(name = "ticker")
    @Id
    private String ticker;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SecurityType type;

    @Column(name = "strike")
    private Double strike;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "expected_return")
    private Double expectedReturn;

    @Column(name = "annualized_sd")
    private Double annualizedSD;

    @Column(name = "underlying_stock")
    private String underlyingStock;

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return ticker;
    }

    public SecurityType getType() {
        return type;
    }

    public void setType(SecurityType type) {
        this.type = type;
    }

    public Double getStrike() {
        return strike;
    }

    public void setStrike(Double strike) {
        this.strike = strike;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Double getExpectedReturn() {
        return expectedReturn;
    }

    public void setExpectedReturn(Double expectedReturn) {
        this.expectedReturn = expectedReturn;
    }

    public Double getAnnualizedSD() {
        return annualizedSD;
    }

    public void setAnnualizedSD(Double annualizedSD) {
        this.annualizedSD = annualizedSD;
    }

    public String getUnderlyingStock() {
        return underlyingStock;
    }

    public void setUnderlyingStock(String underlyingStock) {
        this.underlyingStock = underlyingStock;
    }
}
