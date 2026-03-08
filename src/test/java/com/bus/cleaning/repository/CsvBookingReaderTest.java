package com.bus.cleaning.repository;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvBookingReaderTest {

    private CsvBookingReader reader;

    @BeforeEach
    void setUp() {
        reader = new CsvBookingReader();
    }

    @Test
    void readAll_validCsv_returnsBookings(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("bookings.csv");
        String content = "BookingID,PassengerName,Age,Gender,BusCode,RouteCode,TravelDate,Fare,Status\n"
                + "B1,John Doe,25,M,BUS1,RT1,2024-01-15,500,CONFIRMED\n"
                + "B2,Jane Smith,30,F,BUS2,RT2,2024-02-20,600,CANCELLED\n";
        Files.write(csv, content.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        List<Booking> list = reader.readAll(csv.toString());

        assertEquals(2, list.size());
        Booking b1 = list.get(0);
        assertEquals("B1", b1.bookingId);
        assertEquals("John Doe", b1.passengerName);
        assertEquals("25", b1.age);
        assertEquals("M", b1.gender);
        assertEquals("BUS1", b1.busCode);
        assertEquals("RT1", b1.routeCode);
        assertEquals("2024-01-15", b1.travelDate);
        assertEquals("500", b1.fare);
        assertEquals("CONFIRMED", b1.status);

        Booking b2 = list.get(1);
        assertEquals("B2", b2.bookingId);
        assertEquals("Jane Smith", b2.passengerName);
        assertEquals("600", b2.fare);
        assertEquals("CANCELLED", b2.status);
    }

    @Test
    void readAll_emptyFile_returnsEmptyList(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("empty.csv");
        Files.write(csv, new byte[0]);

        List<Booking> list = reader.readAll(csv.toString());

        assertTrue(list.isEmpty());
    }

    @Test
    void readAll_headerOnly_returnsEmptyList(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("header.csv");
        Files.write(csv, "BookingID,PassengerName,Age,Gender,BusCode,RouteCode,TravelDate,Fare,Status\n".getBytes(java.nio.charset.StandardCharsets.UTF_8));

        List<Booking> list = reader.readAll(csv.toString());

        assertTrue(list.isEmpty());
    }

    @Test
    void readAll_shortRow_skipped(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("mixed.csv");
        String content = "BookingID,PassengerName,Age,Gender,BusCode,RouteCode,TravelDate,Fare,Status\n"
                + "B1,John,25,M,BUS1,RT1,2024-01-01,100,CONFIRMED\n"
                + "short,row\n"
                + "B2,Jane,30,F,BUS2,RT2,2024-02-01,200,CANCELLED\n";
        Files.write(csv, content.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        List<Booking> list = reader.readAll(csv.toString());

        assertEquals(2, list.size());
        assertEquals("B1", list.get(0).bookingId);
        assertEquals("B2", list.get(1).bookingId);
    }

    @Test
    void readAll_missingFile_throws(@TempDir Path tempDir) {
        String path = tempDir.resolve("nonexistent.csv").toString();
        assertThrows(Exception.class, () -> reader.readAll(path));
    }
}
