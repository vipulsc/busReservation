package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateStandardizationRuleTest {

    private DateStandardizationRule rule;

    @BeforeEach
    void setUp() {
        rule = new DateStandardizationRule();
    }

    @Test
    void apply_ddMMyy_setsTravelDateStd() {
        Booking b = new Booking();
        b.travelDate = "15/06/24";

        rule.apply(b);

        assertEquals("2024-06-15", b.travelDateStd);
        assertTrue(b.valid);
    }

    @Test
    void apply_ddMMyyyy_setsTravelDateStd() {
        Booking b = new Booking();
        b.travelDate = "01-12-2023";

        rule.apply(b);

        assertEquals("2023-12-01", b.travelDateStd);
    }

    @Test
    void apply_yyyyMMdd_setsTravelDateStd() {
        Booking b = new Booking();
        b.travelDate = "2024/03/10";

        rule.apply(b);

        assertEquals("2024-03-10", b.travelDateStd);
    }

    @Test
    void apply_isoFormat_setsTravelDateStd() {
        Booking b = new Booking();
        b.travelDate = "2024-01-15";

        rule.apply(b);

        assertEquals("2024-01-15", b.travelDateStd);
    }

    @Test
    void apply_nullDate_addsMissingDate() {
        Booking b = new Booking();
        b.travelDate = null;

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingDate"));
        assertFalse(b.valid);
    }

    @Test
    void apply_emptyDate_addsMissingDate() {
        Booking b = new Booking();
        b.travelDate = "   ";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingDate"));
    }

    @Test
    void apply_invalidFormat_addsInvalidDateFormat() {
        Booking b = new Booking();
        b.travelDate = "not-a-date";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidDateFormat"));
    }

    @Test
    void apply_trimmedDate() {
        Booking b = new Booking();
        b.travelDate = "  2024-05-20  ";

        rule.apply(b);

        assertEquals("2024-05-20", b.travelDateStd);
    }
}
