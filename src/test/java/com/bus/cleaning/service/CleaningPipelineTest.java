package com.bus.cleaning.service;

import com.bus.cleaning.model.Booking;
import com.bus.cleaning.rules.CleaningRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CleaningPipelineTest {

    private CleaningPipeline pipeline;

    @BeforeEach
    void setUp() {
        List<CleaningRule> rules = new ArrayList<>();
        pipeline = new CleaningPipeline(rules);
    }

    @Test
    void process_emptyRules_doesNotChangeBooking() {
        Booking b = new Booking();
        b.bookingId = "B1";
        b.passengerName = "Test";

        pipeline.process(b);

        assertEquals("B1", b.bookingId);
        assertEquals("Test", b.passengerName);
    }

    @Test
    void process_singleRule_appliesRule() {
        List<CleaningRule> rules = new ArrayList<>();
        rules.add(b -> b.passengerName = (b.passengerName != null ? b.passengerName : "").toUpperCase());
        pipeline = new CleaningPipeline(rules);

        Booking b = new Booking();
        b.passengerName = "john";

        pipeline.process(b);

        assertEquals("JOHN", b.passengerName);
    }

    @Test
    void process_multipleRules_appliesInOrder() {
        List<CleaningRule> rules = new ArrayList<>();
        rules.add(b -> b.bookingId = "step1");
        rules.add(b -> b.bookingId = b.bookingId + "-step2");
        pipeline = new CleaningPipeline(rules);

        Booking b = new Booking();
        b.bookingId = "orig";

        pipeline.process(b);

        assertEquals("step1-step2", b.bookingId);
    }
}
