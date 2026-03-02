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

    private static final Logger logger = LogManager.getLogger(AggregationDbRepository.class);

    public int saveAll(AppConfig cfg, List<AggregationRow> rows) throws Exception {

        String sql = "INSERT INTO booking_aggregation " +
                "(route_name, total_bookings, total_revenue, confirmed_count, cancelled_count) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "total_bookings=VALUES(total_bookings), " +
                "total_revenue=VALUES(total_revenue), " +
                "confirmed_count=VALUES(confirmed_count), " +
                "cancelled_count=VALUES(cancelled_count)";

        int count = 0;

        try (Connection conn = DbConfig.getConnection(cfg);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (AggregationRow r : rows) {
                ps.setString(1, r.routeName);
                ps.setInt(2, r.totalBookings);
                ps.setBigDecimal(3, BigDecimal.valueOf(r.totalRevenue));
                ps.setInt(4, r.confirmedCount);
                ps.setInt(5, r.cancelledCount);

                ps.addBatch();
                count++;
            }

            ps.executeBatch();
        }

        logger.info("Aggregation saved to DB. Rows inserted/updated: {}", count);
        return count;
    }
}