# Bus Cleaning Project – Division into 6 Equal Parts

This document divides the project into **6 topic-based parts** so that:

- Each part is **similar in size** (roughly equal lines of code).
- Each part is **one clear topic** that makes sense on its own.
- Together, the 6 parts **explain the entire project** from entry point to outputs.

---

## Summary

| Part | Topic | Files | Approx. lines | What it explains |
|------|--------|-------|----------------|------------------|
| **1** | Application, configuration & data model | 5 | 378 | What the app is, how it’s configured, and the core data structure |
| **2** | Cleaning pipeline & validation rules | 7 | 384 | How the pipeline runs and how we validate/normalize names and numbers |
| **3** | Standardization & mapping rules | 6 | 392 | How we standardize dates/status and map route codes |
| **4** | Ingestion, derived data & writer contract | 6 | 352 | How we read data, derive fields, and define the writer interface |
| **5** | File & database persistence | 3 | 326 | How we write CSVs and persist to the database |
| **6** | Deduplication & aggregation services | 5 | 415 | How we remove duplicates and build route-wise summaries |

**Total: 33 files, ~2,247 lines** (each part ~326–415 lines).

---

## Part 1: Application, configuration & data model  
**~378 lines**

**Topic:** The application entry point, configuration, and the core `Booking` model. This part answers: *What does this app do, how is it configured, and what is the shape of the data?*

| File | Lines | Role |
|------|-------|------|
| `src/main/java/.../app/Application.java` | 134 | Main entry; orchestrates read → clean → dedupe → persist → write |
| `src/main/java/.../config/AppConfig.java` | 44 | Paths and flags (input, output, DB on/off) |
| `src/main/java/.../config/DbConfig.java` | 30 | DB connection settings |
| `src/main/java/.../model/Booking.java` | 59 | Core model: raw fields, cleaned fields, valid/invalidReason |
| `src/test/java/.../model/BookingTest.java` | 111 | Unit tests for `Booking` (CSV row output, rejection reasons) |

---

## Part 2: Cleaning pipeline & validation rules  
**~384 lines**

**Topic:** The cleaning pipeline and the rules that **validate and normalize** (names and numerics). This part answers: *How does the cleaning pipeline work, and how are names and numbers validated/normalized?*

| File | Lines | Role |
|------|-------|------|
| `src/main/java/.../rules/CleaningRule.java` | 6 | Interface: `void apply(Booking b)` |
| `src/main/java/.../service/CleaningPipeline.java` | 20 | Runs a list of rules in sequence |
| `src/main/java/.../rules/NameNormalizationRule.java` | 43 | Trim, proper case, letters/spaces only |
| `src/main/java/.../rules/NumericValidationRule.java` | 41 | Age 0–100, fare ≥ 0, parse checks |
| `src/test/java/.../rules/NameNormalizationRuleTest.java` | 99 | Tests for name normalization |
| `src/test/java/.../rules/NumericValidationRuleTest.java` | 112 | Tests for numeric validation |
| `src/test/java/.../service/CleaningPipelineTest.java` | 63 | Tests for pipeline orchestration |

---

## Part 3: Standardization & mapping rules  
**~392 lines**

**Topic:** Rules that **standardize** (dates, status) and **map** (route codes). This part answers: *How do we standardize dates and status, and how do we map route codes to names?*

| File | Lines | Role |
|------|-------|------|
| `src/main/java/.../rules/DateStandardizationRule.java` | 50 | Multiple date formats → `yyyy-MM-dd` |
| `src/main/java/.../rules/StatusNormalizationRule.java` | 31 | CONFIRMED/CNF/CONF → CONFIRMED; CANCELLED/CANCEL → CANCELLED |
| `src/main/java/.../rules/CodeMappingRule.java` | 44 | Route codes (e.g. RT1–RT6) → route names |
| `src/test/java/.../rules/DateStandardizationRuleTest.java` | 99 | Tests for date standardization |
| `src/test/java/.../rules/StatusNormalizationRuleTest.java` | 81 | Tests for status normalization |
| `src/test/java/.../rules/CodeMappingRuleTest.java` | 87 | Tests for code mapping |

---

## Part 4: Ingestion, derived data & writer contract  
**~352 lines**

**Topic:** **Reading** bookings from CSV, **deriving** age/fare categories, and the **writer interface**. This part answers: *How do we read raw data, derive extra fields, and what is the contract for writing?*

| File | Lines | Role |
|------|-------|------|
| `src/main/java/.../rules/DerivedFieldsRule.java` | 35 | Age category (Minor/Adult/Senior), fare category (Low/Medium/High) |
| `src/test/java/.../rules/DerivedFieldsRuleTest.java` | 129 | Tests for derived fields |
| `src/main/java/.../repository/BookingReader.java` | 7 | Interface: read all bookings |
| `src/main/java/.../repository/CsvBookingReader.java` | 49 | CSV implementation of reader |
| `src/test/java/.../repository/CsvBookingReaderTest.java` | 93 | Tests for CSV reader |
| `src/main/java/.../repository/BookingWriter.java` | 14 | Interface: write cleaned, rejected, aggregation |
| `src/main/java/.../repository/SchemaInitializer.java` | 25 | DB schema creation (tables for bookings & aggregation) |

---

## Part 5: File & database persistence  
**~326 lines**

**Topic:** **Writing** cleaned/rejected/aggregation CSVs and **persisting** to the database. This part answers: *How do we write output files and save data to MySQL?*

| File | Lines | Role |
|------|-------|------|
| `src/main/java/.../repository/CsvBookingWriter.java` | 77 | Writes cleaned.csv, rejected.csv, aggregation.csv |
| `src/test/java/.../repository/CsvBookingWriterTest.java` | 102 | Tests for CSV writer |
| `src/main/java/.../repository/DbBookingRepository.java` | 147 | Saves cleaned bookings to MySQL |

---

## Part 6: Deduplication & aggregation services  
**~415 lines**

**Topic:** **Removing duplicates** and **building route-wise aggregation** (and persisting it). This part answers: *How do we deduplicate valid bookings and produce route summaries?*

| File | Lines | Role |
|------|-------|------|
| `src/main/java/.../repository/AggregationDbRepository.java` | 119 | Saves route-wise aggregation to MySQL |
| `src/main/java/.../service/DuplicateService.java` | 33 | Dedupe by booking ID or composite (name, bus, route, date) |
| `src/test/java/.../service/DuplicateServiceTest.java` | 107 | Tests for deduplication |
| `src/main/java/.../service/AggregationService.java` | 69 | Builds route-wise summary (count, total fare, etc.) |
| `src/test/java/.../service/AggregationServiceTest.java` | 87 | Tests for aggregation |

---

## How the 6 parts tell the full story

1. **Part 1** – What the app is, how it’s configured, and the data model.
2. **Part 2** – How cleaning is orchestrated and how we validate/normalize names and numbers.
3. **Part 3** – How we standardize dates/status and map route codes.
4. **Part 4** – How we read data, derive categories, and define the writer contract (and DB schema).
5. **Part 5** – How we write CSVs and persist to the database.
6. **Part 6** – How we deduplicate and aggregate for reports.

Together they cover: **config → read → clean (pipeline + rules) → dedupe → aggregate → persist & write**.

---

## Balance note

- Parts 1–3 are very even (378, 384, 392).
- Part 4 is slightly smaller (352) because it focuses on a single rule (derived fields), reader, and interfaces.
- Parts 5 and 6 are 326 and 415; Part 6 is slightly larger because it contains both aggregation and deduplication services plus the aggregation DB repository.

Overall, each part is **one clear topic**, **similar in size**, and **able to explain its slice of the project** so the whole system is understandable when all 6 are presented together.
