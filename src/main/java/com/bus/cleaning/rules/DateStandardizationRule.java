package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateStandardizationRule implements CleaningRule {

    private static final Logger logger = LogManager.getLogger(DateStandardizationRule.class);

    private static final DateTimeFormatter[] INPUT_FORMATS = {
            DateTimeFormatter.ofPattern("dd/MM/yy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    private static final DateTimeFormatter OUTPUT_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void apply(Booking b) {

        if (b.travelDate == null || b.travelDate.trim().isEmpty()) {
            b.addReason("MissingDate");
            logger.warn("Missing date for bookingId={}", b.bookingId);
            return;
        }

        String raw = b.travelDate.trim();
        boolean parsed = false;

        for (DateTimeFormatter fmt : INPUT_FORMATS) {
            try {
                LocalDate date = LocalDate.parse(raw, fmt);
                b.travelDateStd = date.format(OUTPUT_FORMAT);
                parsed = true;
                break;
            } catch (DateTimeParseException ignored) {}
        }

        if (!parsed) {
            b.addReason("InvalidDateFormat");
            logger.warn("Invalid date format for bookingId={}", b.bookingId);
        }
    }
}