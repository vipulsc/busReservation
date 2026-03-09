package com.bus.cleaning.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bus.cleaning.config.AppConfig;
import com.bus.cleaning.config.DbConfig;
import com.bus.cleaning.model.Booking;

public class DbBookingRepository {

    private static final Logger logger =
            LogManager.getLogger(DbBookingRepository.class);

    // TABLE CREATION SQL
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS booking (" +
            "booking_id VARCHAR(50) PRIMARY KEY," +
            "passenger_name VARCHAR(100)," +
            "age INT," +
            "gender VARCHAR(10)," +
            "bus_code VARCHAR(50)," +
            "route_code VARCHAR(50)," +
            "route_name VARCHAR(100)," +
            "travel_date DATE," +
            "fare DECIMAL(10,2)," +
            "status VARCHAR(30)," +
            "age_category VARCHAR(30)," +
            "fare_category VARCHAR(30)" +
            ")";

    // INSERT / UPDATE SQL
    private static final String INSERT_SQL =
            "INSERT INTO booking " +
            "(booking_id, passenger_name, age, gender, bus_code, route_code, " +
            "route_name, travel_date, fare, status, age_category, fare_category) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "passenger_name=VALUES(passenger_name)," +
            "age=VALUES(age)," +
            "gender=VALUES(gender)," +
            "bus_code=VALUES(bus_code)," +
            "route_code=VALUES(route_code)," +
            "route_name=VALUES(route_name)," +
            "travel_date=VALUES(travel_date)," +
            "fare=VALUES(fare)," +
            "status=VALUES(status)," +
            "age_category=VALUES(age_category)," +
            "fare_category=VALUES(fare_category)";

    // AUTO CREATE TABLE
    public void createTableIfNotExists(AppConfig cfg) throws Exception {

        try (Connection conn = DbConfig.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(CREATE_TABLE_SQL)) {

            ps.execute();
            logger.info("Booking table verified/created successfully");
        }
    }

    // SAVE ALL BOOKINGS
    public void saveAll(AppConfig cfg, List<Booking> bookings) throws Exception {

        if (bookings == null || bookings.isEmpty()) {
            logger.warn("No bookings available for insert");
            return;
        }

        //  Ensure table exists
        createTableIfNotExists(cfg);

        try (Connection conn = DbConfig.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);

            int batchCount = 0;
            int totalCount = 0;

            for (Booking b : bookings) {

                ps.setString(1, b.bookingId);
                ps.setString(2, b.passengerName);

                if (b.ageValue != null)
                    ps.setInt(3, b.ageValue);
                else
                    ps.setNull(3, java.sql.Types.INTEGER);

                ps.setString(4, b.gender);
                ps.setString(5, b.busCode);
                ps.setString(6, b.routeCode);
                ps.setString(7, b.routeName);

                if (b.travelDateStd != null)
                    ps.setDate(8, Date.valueOf(b.travelDateStd));
                else
                    ps.setNull(8, java.sql.Types.DATE);

                if (b.fareValue != null)
                    ps.setBigDecimal(9,
                            BigDecimal.valueOf(b.fareValue));
                else
                    ps.setNull(9, java.sql.Types.DECIMAL);

                ps.setString(10, b.status);
                ps.setString(11, b.ageCategory);
                ps.setString(12, b.fareCategory);

                ps.addBatch();
                batchCount++;
                totalCount++;

                // batch execution
                if (batchCount == 100) {
                    ps.executeBatch();
                    conn.commit();
                    batchCount = 0;
                }
            }

            // batch execution
            ps.executeBatch();
            conn.commit();

            logger.info("Total rows inserted/updated: {}", totalCount);
        }
        catch (Exception e) {
            logger.error("DB insert failed", e);
            throw e;
        }
    }
}