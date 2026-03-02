package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NameNormalizationRule implements CleaningRule {

    private static final Logger logger = LogManager.getLogger(NameNormalizationRule.class);

    @Override
    public void apply(Booking b) {

        if (b.passengerName == null || b.passengerName.trim().isEmpty()) {
            b.addReason("MissingName");
            logger.warn("Missing name for bookingId={}", b.bookingId);
            return;
        }

        // Trim spaces
        String name = b.passengerName.trim();

        // Validate (only letters and spaces allowed)
        if (!name.matches("[a-zA-Z ]+")) {
            b.addReason("InvalidName");
            logger.warn("Invalid name for bookingId={}", b.bookingId);
            return;
        }

        // Convert to Proper Case
        String[] parts = name.toLowerCase().split(" ");
        StringBuilder proper = new StringBuilder();

        for (String p : parts) {
            if (!p.isEmpty()) {
                proper.append(Character.toUpperCase(p.charAt(0)))
                      .append(p.substring(1))
                      .append(" ");
            }
        }

        b.passengerName = proper.toString().trim();
    }
}