package com.bus.cleaning.repository;

import com.bus.cleaning.config.AppConfig;
import com.bus.cleaning.config.DbConfig;
import com.bus.cleaning.model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class DbBookingRepository {

    private static final Logger logger = LogManager.getLogger(DbBookingRepository.class);

    public void saveAll(AppConfig cfg, List<Booking> bookings) throws Exception {

        String sql = "INSERT INTO booking " +
                "(booking_id, passenger_name, age, gender, bus_code, route_code, route_name, travel_date, fare, status, age_category, fare_category) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "passenger_name=VALUES(passenger_name), age=VALUES(age), gender=VALUES(gender), " +
                "bus_code=VALUES(bus_code), route_code=VALUES(route_code), route_name=VALUES(route_name), " +
                "travel_date=VALUES(travel_date), fare=VALUES(fare), status=VALUES(status), " +
                "age_category=VALUES(age_category), fare_category=VALUES(fare_category)";

        try (Connection conn = DbConfig.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int count = 0;

            for (Booking b : bookings) {
                ps.setString(1, b.bookingId);
                ps.setString(2, b.passengerName);
                ps.setInt(3, b.ageValue == null ? 0 : b.ageValue);
                ps.setString(4, b.gender);
                ps.setString(5, b.busCode);
                ps.setString(6, b.routeCode);
                ps.setString(7, b.routeName);
                ps.setDate(8, b.travelDateStd == null ? null : java.sql.Date.valueOf(b.travelDateStd));
                ps.setBigDecimal(9, b.fareValue == null ? null : java.math.BigDecimal.valueOf(b.fareValue));
                ps.setString(10, b.status);
                ps.setString(11, b.ageCategory);
                ps.setString(12, b.fareCategory);

                ps.addBatch();
                count++;

                if (count % 100 == 0) ps.executeBatch(); // batch insert
            }

            ps.executeBatch();
            logger.info("Inserted/Updated rows in DB: {}", count);
        }
    }
}