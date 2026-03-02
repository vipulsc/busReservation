package com.bus.cleaning.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bus.cleaning.model.Booking;
import com.bus.cleaning.service.AggregationService.AggregationRow;

public class CsvBookingWriter implements BookingWriter {

    private static final Logger logger = LogManager.getLogger(CsvBookingWriter.class);

    private void ensureParentFolder(String filePath) {
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (parent != null) parent.mkdirs();
    }

    @Override
    public void writeCleaned(String path, List<Booking> valid) throws Exception {
        ensureParentFolder(path);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("BookingID,PassengerName,Age,Gender,BusCode,RouteCode,TravelDate,Fare,Status,RouteName,AgeCategory,FareCategory");
            bw.newLine();

            for (Booking b : valid) {
                bw.write(b.toCleanCsvRow());
                bw.newLine();
            }
        }

        logger.info("Cleaned file written: {} | rows={}", path, valid.size());
    }

    @Override
    public void writeRejected(String path, List<Booking> invalid) throws Exception {
        ensureParentFolder(path);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("BookingID,PassengerName,Age,Gender,BusCode,RouteCode,TravelDate,Fare,Status,RouteName,AgeCategory,FareCategory,Reason");
            bw.newLine();

            for (Booking b : invalid) {
                bw.write(b.toRejectedCsvRow());
                bw.newLine();
            }
        }

        logger.info("Rejected file written: {} | rows={}", path, invalid.size());
    }

    @Override
    public void writeAggregation(String path, List<AggregationRow> rows) throws Exception {
        ensureParentFolder(path);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("RouteName,TotalBookings,TotalRevenue,ConfirmedCount,CancelledCount");
            bw.newLine();

            for (AggregationRow r : rows) {
                bw.write(safe(r.routeName) + "," + r.totalBookings + "," + r.totalRevenue + "," + r.confirmedCount + "," + r.cancelledCount);
                bw.newLine();
            }
        }

        logger.info("Aggregation file written: {} | rows={}", path, rows.size());
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}