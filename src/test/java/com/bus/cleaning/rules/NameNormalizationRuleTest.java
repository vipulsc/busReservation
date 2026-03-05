package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameNormalizationRuleTest {

    private NameNormalizationRule rule;

    @BeforeEach
    void setUp() {
        rule = new NameNormalizationRule();
    }

    @Test
    void apply_validName_convertsToProperCase() {
        Booking b = new Booking();
        b.passengerName = "john doe";

        rule.apply(b);

        assertEquals("John Doe", b.passengerName);
        assertTrue(b.valid);
    }

    @Test
    void apply_mixedCase_normalizesToProperCase() {
        Booking b = new Booking();
        b.passengerName = "JANE SMITH";

        rule.apply(b);

        assertEquals("Jane Smith", b.passengerName);
    }

    @Test
    void apply_trimmedWhitespace() {
        Booking b = new Booking();
        b.passengerName = "  alice   bob  ";

        rule.apply(b);

        assertEquals("Alice Bob", b.passengerName);
    }

    @Test
    void apply_nullName_addsMissingName() {
        Booking b = new Booking();
        b.passengerName = null;

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingName"));
        assertFalse(b.valid);
    }

    @Test
    void apply_emptyName_addsMissingName() {
        Booking b = new Booking();
        b.passengerName = "   ";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingName"));
    }

    @Test
    void apply_nameWithNumbers_addsInvalidName() {
        Booking b = new Booking();
        b.passengerName = "John123";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidName"));
    }

    @Test
    void apply_nameWithSpecialChars_addsInvalidName() {
        Booking b = new Booking();
        b.passengerName = "John@Doe";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidName"));
    }

    @Test
    void apply_singleWord_properCase() {
        Booking b = new Booking();
        b.passengerName = "madonna";

        rule.apply(b);

        assertEquals("Madonna", b.passengerName);
    }
}
