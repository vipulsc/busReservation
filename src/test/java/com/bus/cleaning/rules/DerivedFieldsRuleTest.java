package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DerivedFieldsRuleTest {

    private DerivedFieldsRule rule;

    @BeforeEach
    void setUp() {
        rule = new DerivedFieldsRule();
    }

    @Test
    void apply_ageUnder18_setsMinor() {
        Booking b = new Booking();
        b.ageValue = 17;

        rule.apply(b);

        assertEquals("Minor", b.ageCategory);
    }

    @Test
    void apply_age18_setsAdult() {
        Booking b = new Booking();
        b.ageValue = 18;

        rule.apply(b);

        assertEquals("Adult", b.ageCategory);
    }

    @Test
    void apply_age60_setsAdult() {
        Booking b = new Booking();
        b.ageValue = 60;

        rule.apply(b);

        assertEquals("Adult", b.ageCategory);
    }

    @Test
    void apply_ageOver60_setsSenior() {
        Booking b = new Booking();
        b.ageValue = 61;

        rule.apply(b);

        assertEquals("Senior", b.ageCategory);
    }

    @Test
    void apply_nullAge_setsEmptyAgeCategory() {
        Booking b = new Booking();
        b.ageValue = null;

        rule.apply(b);

        assertEquals("", b.ageCategory);
    }

    @Test
    void apply_fareUnder500_setsLow() {
        Booking b = new Booking();
        b.fareValue = 499.0;

        rule.apply(b);

        assertEquals("Low", b.fareCategory);
    }

    @Test
    void apply_fare500_setsMedium() {
        Booking b = new Booking();
        b.fareValue = 500.0;

        rule.apply(b);

        assertEquals("Medium", b.fareCategory);
    }

    @Test
    void apply_fare600_setsMedium() {
        Booking b = new Booking();
        b.fareValue = 600.0;

        rule.apply(b);

        assertEquals("Medium", b.fareCategory);
    }

    @Test
    void apply_fareOver600_setsHigh() {
        Booking b = new Booking();
        b.fareValue = 601.0;

        rule.apply(b);

        assertEquals("High", b.fareCategory);
    }

    @Test
    void apply_nullFare_setsEmptyFareCategory() {
        Booking b = new Booking();
        b.fareValue = null;

        rule.apply(b);

        assertEquals("", b.fareCategory);
    }

    @Test
    void apply_bothAgeAndFare_derivesBoth() {
        Booking b = new Booking();
        b.ageValue = 25;
        b.fareValue = 550.0;

        rule.apply(b);

        assertEquals("Adult", b.ageCategory);
        assertEquals("Medium", b.fareCategory);
    }
}
