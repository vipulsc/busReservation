package com.bus.cleaning.rules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bus.cleaning.model.Booking;

public class StatusNormalizationRule implements CleaningRule {

    private static final Logger logger = LogManager.getLogger(StatusNormalizationRule.class);

    @Override
    public void apply(Booking b) {

        if (b.status == null || b.status.trim().isEmpty()) {
            b.addReason("MissingStatus");
            logger.warn("Missing status for bookingId={}", b.bookingId);
            return;
        }

        String s = b.status.trim().toUpperCase();

        if (s.equals("CONFIRMED") || s.equals("CONFIRM") || s.equals("CNF") || s.equals("CONF")) {
            b.status = "CONFIRMED";
        } else if (s.equals("CANCELLED") || s.equals("CANCEL") || s.equals("CANCELED")) {
            b.status = "CANCELLED";
        } else {
            b.addReason("InvalidStatus");
            logger.warn("Invalid status for bookingId={}", b.bookingId);
        }
    }
}