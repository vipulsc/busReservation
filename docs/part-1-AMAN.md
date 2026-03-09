# Part 1: Application, Configuration & Data Model  
**For Scrum Master & Stakeholders – Short Intro**

Hello / Good afternoon / Good evening. I am **[AMAN]**.

---

## What I Own

I own the **application entry point**, **configuration**, and the **core data model** — basically *what the app is*, *how it’s configured*, and *what shape the data has*.

---

## What I Did

1. **Application (`Application.java`)**  
   I implemented the main flow: read raw CSV → run cleaning pipeline → split valid vs rejected → remove duplicates → optionally save to database → write output files (cleaned, rejected, aggregation). So the entire job is orchestrated from here.

2. **Configuration**  
   - **AppConfig**: Loads input path, output paths (cleaned, rejected, aggregation), and a flag for whether the database is enabled.  
   - **DbConfig**: Holds database URL, username, and password for MySQL.

3. **Data Model (`Booking.java`)**  
   I defined the core `Booking` model: raw fields (bookingId, passengerName, age, gender, busCode, routeCode, travelDate, fare, status), cleaned/standardized fields (ageValue, fareValue, travelDateStd, routeName, ageCategory, fareCategory), and validation state (valid, invalidReason). I also added methods to output a row for cleaned CSV and for rejected CSV with the reason.

4. **Tests**  
   I wrote unit tests for `Booking` to ensure CSV row output and rejection reasons are correct.

---

## One-Liner for the Meet

*"I own the main application flow, all configuration (paths and DB), and the Booking data model that the rest of the pipeline uses."*

---

**Files:** `Application.java`, `AppConfig.java`, `DbConfig.java`, `Booking.java`, `BookingTest.java`

That’s all from my side. Thank you.
