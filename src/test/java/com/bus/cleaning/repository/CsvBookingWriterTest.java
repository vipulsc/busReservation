package com.bus.cleaning.repository;

import com.bus.cleaning.model.Booking;
import com.bus.cleaning.service.AggregationService.AggregationRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvBookingWriterTest {

    private CsvBookingWriter writer;

    @BeforeEach
    void setUp() {
        writer = new CsvBookingWriter();
    }

    @Test
    void writeCleaned_writesHeaderAndRows(@TempDir Path tempDir) throws Exception {
        Path out = tempDir.resolve("subdir").resolve("cleaned.csv");
        List<Booking> valid = new ArrayList<>();
        Booking b = new Booking();
        b.bookingId = "B1";
        b.passengerName = "John Doe";
        b.ageValue = 25;
        b.gender = "M";
        b.busCode = "BUS1";
        b.routeCode = "RT1";
        b.travelDateStd = "2024-01-15";
        b.fareValue = 500.0;
        b.status = "CONFIRMED";
        b.routeName = "Delhi-Jaipur";
        b.ageCategory = "Adult";
        b.fareCategory = "Medium";
        valid.add(b);

        writer.writeCleaned(out.toString(), valid);

        List<String> lines = Files.readAllLines(out);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("BookingID"));
        assertTrue(lines.get(0).contains("RouteName"));
        assertTrue(lines.get(1).contains("B1"));
        assertTrue(lines.get(1).contains("John Doe"));
        assertTrue(lines.get(1).contains("2024-01-15"));
        assertTrue(lines.get(1).contains("Delhi-Jaipur"));
    }

    @Test
    void writeRejected_writesReasonColumn(@TempDir Path tempDir) throws Exception {
        Path out = tempDir.resolve("rejected.csv");
        List<Booking> invalid = new ArrayList<>();
        Booking b = new Booking();
        b.bookingId = "B2";
        b.passengerName = "Jane";
        b.addReason("InvalidStatus");
        invalid.add(b);

        writer.writeRejected(out.toString(), invalid);

        List<String> lines = Files.readAllLines(out);
        assertEquals(2, lines.size());
        assertTrue(lines.get(0).contains("Reason"));
        assertTrue(lines.get(1).contains("InvalidStatus"));
    }

    @Test
    void writeAggregation_writesRouteSummary(@TempDir Path tempDir) throws Exception {
        Path out = tempDir.resolve("agg.csv");
        List<AggregationRow> rows = new ArrayList<>();
        rows.add(new AggregationRow("Delhi-Jaipur", 10, 5500.0, 8, 2));
        rows.add(new AggregationRow("Mumbai-Pune", 5, 3000.0, 4, 1));

        writer.writeAggregation(out.toString(), rows);

        List<String> lines = Files.readAllLines(out);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("RouteName"));
        assertTrue(lines.get(0).contains("TotalRevenue"));
        assertTrue(lines.get(1).contains("Delhi-Jaipur"));
        assertTrue(lines.get(1).contains("10"));
        assertTrue(lines.get(1).contains("5500"));
        assertTrue(lines.get(2).contains("Mumbai-Pune"));
    }

    @Test
    void writeCleaned_emptyList_writesOnlyHeader(@TempDir Path tempDir) throws Exception {
        Path out = tempDir.resolve("empty.csv");
        writer.writeCleaned(out.toString(), new ArrayList<>());

        List<String> lines = Files.readAllLines(out);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).startsWith("BookingID"));
    }
}
