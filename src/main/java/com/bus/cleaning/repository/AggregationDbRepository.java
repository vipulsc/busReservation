package com.bus.cleaning.repository;

import com.bus.cleaning.config.AppConfig;
import com.bus.cleaning.config.DbConfig;
import com.bus.cleaning.service.AggregationService.AggregationRow;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class AggregationDbRepository {

    private static final Logger logger =
            LogManager.getLogger(AggregationDbRepository.class);

    // =====================================
    // AUTO CREATE AGGREGATION TABLE
    // =====================================
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS booking_aggregation (" +
            "route_name VARCHAR(100) PRIMARY KEY," +
            "total_bookings INT," +
            "total_revenue DECIMAL(12,2)," +
            "confirmed_count INT," +
            "cancelled_count INT" +
            ")";

    // =====================================
    // UPSERT SQL
    // =====================================
    private static final String INSERT_SQL =
            "INSERT INTO booking_aggregation " +
            "(route_name, total_bookings, total_revenue, confirmed_count, cancelled_count) " +
            "VALUES (?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "total_bookings=VALUES(total_bookings)," +
            "total_revenue=VALUES(total_revenue)," +
            "confirmed_count=VALUES(confirmed_count)," +
            "cancelled_count=VALUES(cancelled_count)";

    // =====================================
    // CREATE TABLE IF NOT EXISTS
    // =====================================
    public void createTableIfNotExists(AppConfig cfg) throws Exception {

        try (Connection conn = DbConfig.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(CREATE_TABLE_SQL)) {

            ps.execute();
            logger.info("booking_aggregation table verified/created");
        }
    }

    // =====================================
    // SAVE AGGREGATION DATA
    // =====================================
    public int saveAll(AppConfig cfg,
                       List<AggregationRow> rows) throws Exception {

        if (rows == null || rows.isEmpty()) {
            logger.warn("No aggregation rows to save");
            return 0;
        }

        // ✅ Ensure table exists
        createTableIfNotExists(cfg);

        int totalCount = 0;
        int batchCount = 0;

        try (Connection conn = DbConfig.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);

            for (AggregationRow r : rows) {

                ps.setString(1, r.routeName);
                ps.setInt(2, r.totalBookings);

                ps.setBigDecimal(
                        3,
                        BigDecimal.valueOf(r.totalRevenue)
                );

                ps.setInt(4, r.confirmedCount);
                ps.setInt(5, r.cancelledCount);

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

            ps.executeBatch();
            conn.commit();
        }
        catch (Exception e) {
            logger.error("Aggregation DB insert failed", e);
            throw e;
        }

        logger.info(
                "Aggregation saved successfully. Rows inserted/updated: {}",
                totalCount
        );

        return totalCount;
    }
}