package com.stock.realtime.rtstock.position;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class PositionCSVReader {

    private static final String COMMA_DELIMITER = ",";

    @Value("classpath:positions.csv")
    private Resource positionsCsv;

    public List<Position> readCsv() throws IOException {
        List<Position> positions = new ArrayList<>();

        try (InputStream inputStream = positionsCsv.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] record = line.split(COMMA_DELIMITER);
                positions.add(new Position(record[0], Integer.parseInt(record[1])));
            }
        }

        return positions;
    }
}
