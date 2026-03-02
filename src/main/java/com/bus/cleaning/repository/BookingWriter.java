package com.bus.cleaning.repository;

import java.util.List;

import com.bus.cleaning.model.Booking;
import com.bus.cleaning.service.AggregationService.AggregationRow;

public interface BookingWriter {

    void writeCleaned(String path, List<Booking> valid) throws Exception;

    void writeRejected(String path, List<Booking> invalid) throws Exception;

    void writeAggregation(String path, List<AggregationRow> rows) throws Exception;
}