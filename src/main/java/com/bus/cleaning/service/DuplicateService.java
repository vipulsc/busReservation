package com.bus.cleaning.service;

import com.bus.cleaning.model.Booking;

import java.util.*;

public class DuplicateService {

	// Remove duplicates
	public List<Booking> removeDuplicates(List<Booking> validBookings) {

		Map<String, Booking> unique = new LinkedHashMap<>();

		for (Booking b : validBookings) {
			String key = buildKey(b);
			if (!unique.containsKey(key)) {
				unique.put(key, b);
			}
		}
		return new ArrayList<>(unique.values());
	}

	private String buildKey(Booking b) {
		String id = (b.bookingId == null) ? "" : b.bookingId.trim();
		if (!id.isEmpty())
			return "ID:" + id;

		String name = (b.passengerName == null) ? "" : b.passengerName.trim().toUpperCase();
		String bus = (b.busCode == null) ? "" : b.busCode.trim().toUpperCase();
		String route = (b.routeCode == null) ? "" : b.routeCode.trim().toUpperCase();
		String date = (b.travelDateStd == null) ? "" : b.travelDateStd.trim();
		return "CMP:" + name + "|" + bus + "|" + route + "|" + date;
	}
}