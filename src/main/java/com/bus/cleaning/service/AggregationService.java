package com.bus.cleaning.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bus.cleaning.model.Booking;

public class AggregationService {

    // DTO row for aggregation report
    public static class AggregationRow {
        public final String routeName;
        public final int totalBookings;
        public final double totalRevenue;
        public final int confirmedCount;
        public final int cancelledCount;

        public AggregationRow(String routeName, int totalBookings, double totalRevenue,
                              int confirmedCount, int cancelledCount) {
            this.routeName = routeName;
            this.totalBookings = totalBookings;
            this.totalRevenue = totalRevenue;
            this.confirmedCount = confirmedCount;
            this.cancelledCount = cancelledCount;
        }
    }

    // Use Case 9: Aggregation (Route-wise)
    public List<AggregationRow> routeWiseSummary(List<Booking> bookings) {

        // Manual grouping (no Collectors) => avoids all generic inference issues
        Map<String, List<Booking>> grouped = new HashMap<>();

        for (Booking b : bookings) {
            String key = (b.routeName == null || b.routeName.trim().isEmpty())
                    ? "UNKNOWN"
                    : b.routeName.trim();

            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(b);
        }

        List<AggregationRow> rows = new ArrayList<>();

        for (Map.Entry<String, List<Booking>> entry : grouped.entrySet()) {
            String route = entry.getKey();
            List<Booking> list = entry.getValue();

            int total = list.size();

            double revenue = 0.0;
            int conf = 0;
            int canc = 0;

            for (Booking b : list) {
                if (b.fareValue != null) revenue += b.fareValue;
                if ("CONFIRMED".equalsIgnoreCase(b.status)) conf++;
                if ("CANCELLED".equalsIgnoreCase(b.status)) canc++;
            }

            rows.add(new AggregationRow(route, total, revenue, conf, canc));
        }

        // Sort by revenue descending
        rows.sort(Comparator.comparingDouble((AggregationRow r) -> r.totalRevenue).reversed());
        return rows;
    }
}