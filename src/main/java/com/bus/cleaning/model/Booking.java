package com.bus.cleaning.model;

public class Booking {
	public String bookingId;
	public String passengerName;
	public String age;
	public String gender;
	public String busCode;
	public String routeCode;
	public String travelDate;
	public String fare;
	public String status;

	// Cleaned / standardized fields
	public Integer ageValue;
	public Double fareValue;
	public String travelDateStd; // yyyy-MM-dd
	public String routeName; // mapped from routeCode
	public String ageCategory; // derived
	public String fareCategory; // derived

	public boolean valid = true;
	public String invalidReason = "";

	public void addReason(String reason) {
		if (invalidReason == null || invalidReason.isEmpty())
			invalidReason = reason;
		else
			invalidReason += "|" + reason;
		valid = false;
	}

	private String safe(String s) {
		return s == null ? "" : s.trim();
	}

	private String outAge() {
		return ageValue == null ? safe(age) : String.valueOf(ageValue);
	}

	private String outFare() {
		if (fareValue == null)
			return safe(fare);
		if (fareValue.doubleValue() == Math.rint(fareValue.doubleValue()))
			return String.valueOf(fareValue.intValue());
		return String.valueOf(fareValue);
	}

	public String toCleanCsvRow() {
		String dateOut = (travelDateStd != null && !travelDateStd.isBlank()) ? travelDateStd : safe(travelDate);

		return safe(bookingId) + "," + safe(passengerName) + "," + outAge() + "," + safe(gender) + "," + safe(busCode)
				+ "," + safe(routeCode) + "," + dateOut + "," + outFare() + "," + safe(status) + "," + safe(routeName)
				+ "," + safe(ageCategory) + "," + safe(fareCategory);
	}

	public String toRejectedCsvRow() {
		return toCleanCsvRow() + "," + safe(invalidReason);
	}
}