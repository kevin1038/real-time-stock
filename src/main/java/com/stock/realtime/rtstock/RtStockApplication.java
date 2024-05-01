package com.stock.realtime.rtstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RtStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtStockApplication.class, args);
    }

}
