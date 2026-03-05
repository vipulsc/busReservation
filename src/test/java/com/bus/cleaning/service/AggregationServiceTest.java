package com.bus.cleaning.service;

import com.bus.cleaning.model.Booking;
import com.bus.cleaning.service.AggregationService.AggregationRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AggregationServiceTest {

    private AggregationService service;

    @BeforeEach
    void setUp() {
        service = new AggregationService();
    }

    @Test
    void routeWiseSummary_groupsByRouteName() {
        List<Booking> list = new ArrayList<>();
        list.add(booking("Delhi-Jaipur", 500.0, "CONFIRMED"));
        list.add(booking("Delhi-Jaipur", 600.0, "CONFIRMED"));
        list.add(booking("Mumbai-Pune", 400.0, "CANCELLED"));

        List<AggregationRow> rows = service.routeWiseSummary(list);

        assertEquals(2, rows.size());
        AggregationRow delhi = rows.stream().filter(r -> "Delhi-Jaipur".equals(r.routeName)).findFirst().orElse(null);
        AggregationRow mumbai = rows.stream().filter(r -> "Mumbai-Pune".equals(r.routeName)).findFirst().orElse(null);
        assertNotNull(delhi);
        assertNotNull(mumbai);
        assertEquals(2, delhi.totalBookings);
        assertEquals(1100.0, delhi.totalRevenue);
        assertEquals(2, delhi.confirmedCount);
        assertEquals(0, delhi.cancelledCount);
        assertEquals(1, mumbai.totalBookings);
        assertEquals(1, mumbai.cancelledCount);
    }

    @Test
    void routeWiseSummary_sortedByRevenueDescending() {
        List<Booking> list = new ArrayList<>();
        list.add(booking("Low", 100.0, "CONFIRMED"));
        list.add(booking("High", 1000.0, "CONFIRMED"));
        list.add(booking("Mid", 500.0, "CONFIRMED"));

        List<AggregationRow> rows = service.routeWiseSummary(list);

        assertEquals(1000.0, rows.get(0).totalRevenue);
        assertEquals(500.0, rows.get(1).totalRevenue);
        assertEquals(100.0, rows.get(2).totalRevenue);
    }

    @Test
    void routeWiseSummary_nullRouteName_groupedAsUnknown() {
        List<Booking> list = new ArrayList<>();
        Booking b = new Booking();
        b.routeName = null;
        b.fareValue = 200.0;
        b.status = "CONFIRMED";
        list.add(b);

        List<AggregationRow> rows = service.routeWiseSummary(list);

        assertEquals(1, rows.size());
        assertEquals("UNKNOWN", rows.get(0).routeName);
        assertEquals(200.0, rows.get(0).totalRevenue);
    }

    @Test
    void routeWiseSummary_emptyList_returnsEmpty() {
        List<AggregationRow> rows = service.routeWiseSummary(new ArrayList<>());
        assertTrue(rows.isEmpty());
    }

    private static Booking booking(String routeName, double fare, String status) {
        Booking b = new Booking();
        b.routeName = routeName;
        b.fareValue = fare;
        b.status = status;
        return b;
    }
}
