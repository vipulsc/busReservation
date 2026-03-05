package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusNormalizationRuleTest {

    private StatusNormalizationRule rule;

    @BeforeEach
    void setUp() {
        rule = new StatusNormalizationRule();
    }

    @Test
    void apply_confirmedVariants_normalizedToCONFIRMED() {
        String[] variants = { "CONFIRMED", "confirm", "CNF", "CONF", "Confirmed" };
        for (String input : variants) {
            Booking b = new Booking();
            b.status = input;
            rule.apply(b);
            assertEquals("CONFIRMED", b.status, "Failed for: " + input);
        }
    }

    @Test
    void apply_cancelledVariants_normalizedToCANCELLED() {
        String[] variants = { "CANCELLED", "cancel", "CANCELED" };
        for (String input : variants) {
            Booking b = new Booking();
            b.status = input;
            rule.apply(b);
            assertEquals("CANCELLED", b.status, "Failed for: " + input);
        }
    }

    @Test
    void apply_nullStatus_addsMissingStatus() {
        Booking b = new Booking();
        b.status = null;

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingStatus"));
        assertFalse(b.valid);
    }

    @Test
    void apply_emptyStatus_addsMissingStatus() {
        Booking b = new Booking();
        b.status = "   ";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingStatus"));
    }

    @Test
    void apply_invalidStatus_addsInvalidStatus() {
        Booking b = new Booking();
        b.status = "PENDING";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("InvalidStatus"));
        assertFalse(b.valid);
    }

    @Test
    void apply_trimmedStatus() {
        Booking b = new Booking();
        b.status = "  CONFIRMED  ";

        rule.apply(b);

        assertEquals("CONFIRMED", b.status);
    }
}
