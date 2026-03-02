package com.bus.cleaning.rules;

import com.bus.cleaning.model.Booking;

public class DerivedFieldsRule implements CleaningRule {

	@Override
	public void apply(Booking b) {

		// Age Category
		if (b.ageValue != null) {
			int a = b.ageValue;
			if (a < 18)
				b.ageCategory = "Minor";
			else if (a <= 60)
				b.ageCategory = "Adult";
			else
				b.ageCategory = "Senior";
		} else {
			b.ageCategory = "";
		}

		// Fare Category
		if (b.fareValue != null) {
			double f = b.fareValue;
			if (f < 500)
				b.fareCategory = "Low";
			else if (f <= 600)
				b.fareCategory = "Medium";
			else
				b.fareCategory = "High";
		} else {
			b.fareCategory = "";
		}
	}
}