package com.bus.cleaning.service;

import com.bus.cleaning.model.Booking;
import com.bus.cleaning.rules.CleaningRule;

import java.util.List;

public class CleaningPipeline {

    private final List<CleaningRule> rules;

    public CleaningPipeline(List<CleaningRule> rules) {
        this.rules = rules;
    }

    public void process(Booking b) {
        for (CleaningRule r : rules) {
            r.apply(b);
        }
    }
}