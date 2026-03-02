package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CodeMappingRule implements CleaningRule {

    private static final Logger logger = LogManager.getLogger(CodeMappingRule.class);

    private static final Map<String, String> ROUTE_MAP = new HashMap<>();

    static {
        ROUTE_MAP.put("RT1", "Delhi-Jaipur");
        ROUTE_MAP.put("RT2", "Mumbai-Pune");
        ROUTE_MAP.put("RT3", "Bangalore-Chennai");
        ROUTE_MAP.put("RT4", "Hyderabad-Goa");
        ROUTE_MAP.put("RT5", "Kolkata-Patna");
        ROUTE_MAP.put("RT6", "Ahmedabad-Surat");
    }

    @Override
    public void apply(Booking b) {

        if (b.routeCode == null || b.routeCode.trim().isEmpty()) {
            b.addReason("MissingRouteCode");
            logger.warn("Missing routeCode for bookingId={}", b.bookingId);
            return;
        }

        String code = b.routeCode.trim().toUpperCase();
        String routeName = ROUTE_MAP.get(code);

        if (routeName == null) {
            b.addReason("UnknownRouteCode");
            logger.warn("Unknown routeCode={} for bookingId={}", code, b.bookingId);
            return;
        }

        b.routeName = routeName;
    }
}