package com.bus.cleaning.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void addReason_setsValidFalseAndStoresReason() {
        Booking b = new Booking();
        assertTrue(b.valid);
        assertTrue(b.invalidReason.isEmpty());

        b.addReason("InvalidAge");
        assertFalse(b.valid);
        assertEquals("InvalidAge", b.invalidReason);
    }

    @Test
    void addReason_appendsSecondReasonWithPipe() {
        Booking b = new Booking();
        b.addReason("Reason1");
        b.addReason("Reason2");
        assertEquals("Reason1|Reason2", b.invalidReason);
        assertFalse(b.valid);
    }

    @Test
    void toCleanCsvRow_usesCleanedFieldsWhenPresent() {
        Booking b = new Booking();
        b.bookingId = "B1";
        b.passengerName = "John Doe";
        b.age = "25";
        b.ageValue = 25;
        b.gender = "M";
        b.busCode = "BUS1";
        b.routeCode = "RT1";
        b.travelDate = "01/01/24";
        b.travelDateStd = "2024-01-01";
        b.fare = "500.5";
        b.fareValue = 500.5;
        b.status = "CONFIRMED";
        b.routeName = "Delhi-Jaipur";
        b.ageCategory = "Adult";
        b.fareCategory = "Medium";

        String row = b.toCleanCsvRow();
        assertTrue(row.contains("B1"));
        assertTrue(row.contains("John Doe"));
        assertTrue(row.contains("25"));
        assertTrue(row.contains("2024-01-01"));
        assertTrue(row.contains("500.5"));
        assertTrue(row.contains("Delhi-Jaipur"));
        assertTrue(row.contains("Adult"));
        assertTrue(row.contains("Medium"));
    }

    @Test
    void toCleanCsvRow_usesRawAgeAndFareWhenCleanedNull() {
        Booking b = new Booking();
        b.bookingId = "B2";
        b.passengerName = "Jane";
        b.age = "30";
        b.gender = "F";
        b.busCode = "B2";
        b.routeCode = "RT2";
        b.travelDate = "2024-06-15";
        b.fare = "600";
        b.status = "CONFIRMED";
        b.ageValue = null;
        b.fareValue = null;

        String row = b.toCleanCsvRow();
        assertTrue(row.contains("30"));
        assertTrue(row.contains("600"));
    }

    @Test
    void toCleanCsvRow_handlesNullAndTrim() {
        Booking b = new Booking();
        b.bookingId = "B3";
        b.passengerName = "  Bob  ";
        b.travelDateStd = null;
        b.travelDate = " 2024-01-01 ";
        String row = b.toCleanCsvRow();
        assertNotNull(row);
        assertTrue(row.contains("2024-01-01"));
    }

    @Test
    void toRejectedCsvRow_includesReason() {
        Booking b = new Booking();
        b.bookingId = "B4";
        b.passengerName = "Test";
        b.addReason("InvalidStatus");
        String row = b.toRejectedCsvRow();
        assertTrue(row.contains("InvalidStatus"));
    }

    @Test
    void toCleanCsvRow_integerFareFormattedWithoutDecimals() {
        Booking b = new Booking();
        b.bookingId = "B5";
        b.passengerName = "X";
        b.fareValue = 500.0;
        String row = b.toCleanCsvRow();
        assertTrue(row.contains("500"));
        assertFalse(row.contains("500.0"));
    }
}
