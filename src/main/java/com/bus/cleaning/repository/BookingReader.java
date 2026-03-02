package com.bus.cleaning.repository;

import java.util.List;
import com.bus.cleaning.model.Booking;

public interface BookingReader {
    List<Booking> readAll(String path) throws Exception;
}