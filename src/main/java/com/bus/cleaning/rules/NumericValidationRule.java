package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumericValidationRule implements CleaningRule {

    private static final Logger logger = LogManager.getLogger(NumericValidationRule.class);

    @Override
    public void apply(Booking b) {

        // ---- AGE VALIDATION ----
        try {
            int age = Integer.parseInt(b.age.trim());
            if (age < 0 || age > 100) {
                b.addReason("InvalidAgeRange");
                logger.warn("Invalid age range for bookingId={}", b.bookingId);
            } else {
                b.ageValue = age;
            }
        } catch (Exception e) {
            b.addReason("InvalidAgeFormat");
            logger.warn("Invalid age format for bookingId={}", b.bookingId);
        }

        // ---- FARE VALIDATION ----
        try {
            double fare = Double.parseDouble(b.fare.trim());
            if (fare < 0) {
                b.addReason("NegativeFare");
                logger.warn("Negative fare for bookingId={}", b.bookingId);
            } else {
                b.fareValue = fare;
            }
        } catch (Exception e) {
            b.addReason("InvalidFareFormat");
            logger.warn("Invalid fare format for bookingId={}", b.bookingId);
        }
    }
}