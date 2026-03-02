package com.bus.cleaning.repository;

import com.bus.cleaning.model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvBookingReader implements BookingReader {

    private static final Logger logger = LogManager.getLogger(CsvBookingReader.class);

    @Override
    public List<Booking> readAll(String path) throws Exception {
        List<Booking> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String header = br.readLine(); // skip header
            if (header == null) return list;

            String line;
            while ((line = br.readLine()) != null) {
                String[] c = line.split(",", -1);
                if (c.length < 9) {
                    logger.warn("Skipping bad row (columns < 9): {}", line);
                    continue;
                }

                Booking b = new Booking();
                b.bookingId = c[0].trim();
                b.passengerName = c[1];
                b.age = c[2];
                b.gender = c[3];
                b.busCode = c[4];
                b.routeCode = c[5];
                b.travelDate = c[6];
                b.fare = c[7];
                b.status = c[8];

                list.add(b);
            }
        }

        logger.info("CSV read completed. Records: {}", list.size());
        return list;
    }
}