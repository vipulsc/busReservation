# Part 5: File & Database Persistence  
**For Scrum Master & Stakeholders – Short Intro**

Hello / Good afternoon / Good evening. I am **[Your Name]**.

---

## What I Own

I own **writing** the output files (cleaned, rejected, aggregation CSVs) and **persisting** cleaned bookings to MySQL — so the results of the job are saved to disk and to the database.

---

## What I Did

1. **CsvBookingWriter**  
   I implemented the `BookingWriter` interface for file output. The job writes three CSVs: **cleaned.csv** (valid bookings), **rejected.csv** (invalid ones with reason), and **aggregation.csv** (route-wise summary). Format and headers match what the rest of the system expects.

2. **DbBookingRepository**  
   When the database is enabled, I save each cleaned booking to MySQL. I use the config (URL, user, password) and insert into the bookings table that SchemaInitializer creates. So the same data that goes to cleaned.csv is also available in the DB for other systems or reports.

3. **Tests**  
   I wrote tests for the CSV writer to ensure files are written correctly and content is as expected.

---

## One-Liner for the Meet

*"I implement writing the three output CSVs (cleaned, rejected, aggregation) and saving the cleaned bookings to MySQL so the job’s results are persisted to files and the database."*

---

**Files:** `CsvBookingWriter.java`, `DbBookingRepository.java`, plus CSV writer tests.

That’s all from my side. Thank you.
