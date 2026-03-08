package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeMappingRuleTest {

    private CodeMappingRule rule;

    @BeforeEach
    void setUp() {
        rule = new CodeMappingRule();
    }

    @Test
    void apply_knownRouteCode_setsRouteName() {
        Booking b = new Booking();
        b.routeCode = "RT1";

        rule.apply(b);

        assertEquals("Delhi-Jaipur", b.routeName);
        assertTrue(b.valid);
    }

    @Test
    void apply_lowercaseRouteCode_mapsCorrectly() {
        Booking b = new Booking();
        b.routeCode = "rt2";

        rule.apply(b);

        assertEquals("Mumbai-Pune", b.routeName);
    }

    @Test
    void apply_allKnownCodes_mapped() {
        String[][] codes = {
            { "RT1", "Delhi-Jaipur" },
            { "RT3", "Bangalore-Chennai" },
            { "RT4", "Hyderabad-Goa" },
            { "RT5", "Kolkata-Patna" },
            { "RT6", "Ahmedabad-Surat" }
        };
        for (String[] pair : codes) {
            Booking b = new Booking();
            b.routeCode = pair[0];
            rule.apply(b);
            assertEquals(pair[1], b.routeName, "Failed for " + pair[0]);
        }
    }

    @Test
    void apply_nullRouteCode_addsMissingRouteCode() {
        Booking b = new Booking();
        b.routeCode = null;

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingRouteCode"));
        assertFalse(b.valid);
    }

    @Test
    void apply_emptyRouteCode_addsMissingRouteCode() {
        Booking b = new Booking();
        b.routeCode = "   ";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("MissingRouteCode"));
    }

    @Test
    void apply_unknownRouteCode_addsUnknownRouteCode() {
        Booking b = new Booking();
        b.routeCode = "RT99";

        rule.apply(b);

        assertTrue(b.invalidReason.contains("UnknownRouteCode"));
        assertNull(b.routeName);
    }
}
