package com.bus.cleaning.service;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateServiceTest {

    private DuplicateService service;

    @BeforeEach
    void setUp() {
        service = new DuplicateService();
    }

    @Test
    void removeDuplicates_sameBookingId_keepsFirst() {
        List<Booking> list = new ArrayList<>();
        Booking b1 = booking("B1", "Alice", "BUS1", "RT1", "2024-01-01");
        Booking b2 = booking("B1", "Alice", "BUS1", "RT1", "2024-01-01");
        list.add(b1);
        list.add(b2);

        List<Booking> result = service.removeDuplicates(list);

        assertEquals(1, result.size());
        assertSame(b1, result.get(0));
    }

    @Test
    void removeDuplicates_differentBookingIds_keepsAll() {
        List<Booking> list = new ArrayList<>();
        list.add(booking("B1", "Alice", "BUS1", "RT1", "2024-01-01"));
        list.add(booking("B2", "Bob", "BUS1", "RT1", "2024-01-01"));

        List<Booking> result = service.removeDuplicates(list);

        assertEquals(2, result.size());
    }

    @Test
    void removeDuplicates_noBookingId_usesCompositeKey() {
        List<Booking> list = new ArrayList<>();
        Booking b1 = new Booking();
        b1.bookingId = "";
        b1.passengerName = "Alice";
        b1.busCode = "BUS1";
        b1.routeCode = "RT1";
        b1.travelDateStd = "2024-01-01";
        Booking b2 = new Booking();
        b2.bookingId = "";
        b2.passengerName = "Alice";
        b2.busCode = "BUS1";
        b2.routeCode = "RT1";
        b2.travelDateStd = "2024-01-01";
        list.add(b1);
        list.add(b2);

        List<Booking> result = service.removeDuplicates(list);

        assertEquals(1, result.size());
    }

    @Test
    void removeDuplicates_differentCompositeKey_keepsBoth() {
        List<Booking> list = new ArrayList<>();
        Booking b1 = new Booking();
        b1.bookingId = "";
        b1.passengerName = "Alice";
        b1.busCode = "BUS1";
        b1.routeCode = "RT1";
        b1.travelDateStd = "2024-01-01";
        Booking b2 = new Booking();
        b2.bookingId = "";
        b2.passengerName = "Bob";
        b2.busCode = "BUS1";
        b2.routeCode = "RT1";
        b2.travelDateStd = "2024-01-01";
        list.add(b1);
        list.add(b2);

        List<Booking> result = service.removeDuplicates(list);

        assertEquals(2, result.size());
    }

    @Test
    void removeDuplicates_emptyList_returnsEmpty() {
        List<Booking> result = service.removeDuplicates(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    private static Booking booking(String id, String name, String bus, String route, String date) {
        Booking b = new Booking();
        b.bookingId = id;
        b.passengerName = name;
        b.busCode = bus;
        b.routeCode = route;
        b.travelDateStd = date;
        return b;
    }
}
