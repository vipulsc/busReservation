package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericValidationRuleTest {

    private NumericValidationRule rule;

    @BeforeEach
    void setUp() {
        rule = new NumericValidationRule();
    }

    @Test
    void apply_validAgeAndFare_setsAgeValueAndFareValue() {
        Booking b = new Booking();
        b.bookingId = "B1";
        b.age = "25";
        b.fare = "500.50";

        rule.apply(b);

        assertEquals(25, b.ageValue);
        assertEquals(500.50, b.fareValue);
        assertTrue(b.valid);
    }

    @Test
    void apply_ageOutOfRange_addsInvalidAgeRange() {
        Booking b = new Booking();
        b.age = "150";
        b.fare = "100";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidAgeRange"));
        assertFalse(b.valid);
    }

    @Test
    void apply_negativeAge_addsInvalidAgeRange() {
        Booking b = new Booking();
        b.age = "-5";
        b.fare = "100";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidAgeRange"));
    }

    @Test
    void apply_invalidAgeFormat_addsInvalidAgeFormat() {
        Booking b = new Booking();
        b.age = "abc";
        b.fare = "100";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidAgeFormat"));
        assertFalse(b.valid);
    }

    @Test
    void apply_negativeFare_addsNegativeFare() {
        Booking b = new Booking();
        b.age = "30";
        b.fare = "-100";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("NegativeFare"));
        assertNull(b.fareValue);
    }

    @Test
    void apply_invalidFareFormat_addsInvalidFareFormat() {
        Booking b = new Booking();
        b.age = "30";
        b.fare = "not-a-number";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidFareFormat"));
    }

    @Test
    void apply_ageZeroAndFareZero_accepted() {
        Booking b = new Booking();
        b.age = "0";
        b.fare = "0";

        rule.apply(b);

        assertEquals(0, b.ageValue);
        assertEquals(0.0, b.fareValue);
    }

    @Test
    void apply_age100_accepted() {
        Booking b = new Booking();
        b.age = "100";
        b.fare = "50";

        rule.apply(b);

        assertEquals(100, b.ageValue);
    }
}
