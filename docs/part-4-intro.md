# Part 4: Ingestion, Derived Data & Writer Contract  
**For Scrum Master & Stakeholders – Short Intro**

Hello / Good afternoon / Good evening. I am **[Your Name]**.

---

## What I Own

I own **reading** raw data from CSV, **deriving** age and fare categories, the **writer interface** (contract for writing outputs), and the **DB schema** definition — so we have a clear contract for “how data comes in” and “how we write it out.”

---

## What I Did

1. **BookingReader interface & CsvBookingReader**  
   I defined the `BookingReader` interface (e.g. read all bookings) and implemented **CsvBookingReader** to parse the raw CSV and return a list of `Booking` objects. So the rest of the app doesn’t depend on CSV format directly.

2. **DerivedFieldsRule**  
   After validation and standardization, I derive: **age category** (Minor / Adult / Senior based on age) and **fare category** (Low / Medium / High based on fare). These are used in reporting and analytics.

3. **BookingWriter interface**  
   I defined the writer contract: write cleaned bookings, write rejected bookings (with reason), and write aggregation. So whoever implements persistence (CSV or DB) follows the same contract.

4. **SchemaInitializer**  
   I defined the database schema (tables for bookings and aggregation) so the DB structure is created consistently when DB is enabled.

5. **Tests**  
   I wrote tests for the CSV reader and for the derived-fields rule.

---

## One-Liner for the Meet

*"I own reading raw CSV into bookings, deriving age and fare categories, the writer interface for all outputs, and the DB schema so ingestion and output contracts are clear and testable."*

---

**Files:** `DerivedFieldsRule.java`, `BookingReader.java`, `CsvBookingReader.java`, `BookingWriter.java`, `SchemaInitializer.java`, plus related tests.

That’s all from my side. Thank you.
