package com.stock.realtime.rtstock.position;

public class Position {

    private String symbol;

    private int positionSize;

    public Position(String symbol, int positionSize) {
        this.symbol = symbol;
        this.positionSize = positionSize;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getPositionSize() {
        return positionSize;
    }

    public void setPositionSize(int positionSize) {
        this.positionSize = positionSize;
    }
}
