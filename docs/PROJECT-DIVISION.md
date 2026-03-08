# Bus Cleaning Project — Division into 6 Equal Parts

This document divides the **bus-cleaning** project into **6 equal parts** by responsibility and scope. Each part is a self-contained work package (e.g. for sprint ownership or presentation).

---

## Summary

| Part | Name                         | Files | Approx. lines | Scope                    |
|------|------------------------------|-------|----------------|--------------------------|
| **1** | Foundation                   | 6     | ~165           | Model, config, contract  |
| **2** | Input/Output                 | 4     | ~147           | CSV read & write         |
| **3** | Cleaning rules (validation)  | 3     | ~134           | Name, numeric, date      |
| **4** | Cleaning rules (mapping)     | 4     | ~130           | Status, codes, derived, pipeline |
| **5** | Services & orchestration     | 4     | ~261           | Duplicate, aggregation, schema, app |
| **6** | Persistence & build          | 4     | ~296           | DB repos, Maven          |

**Total:** 25 items across 6 parts (~1,133 lines of code + config).

---

## Part 1 — Foundation (Model & configuration)

**Purpose:** Data model, application configuration, and the cleaning-rule contract.

| File | Location | Role |
|------|----------|------|
| `Booking.java` | `model/` | Core domain model (fields, valid flag, rejection reasons, CSV serialization) |
| `AppConfig.java` | `config/` | Loads input/output paths, DB toggle, DB URL/user/password from properties |
| `DbConfig.java` | `config/` | JDBC connection creation (server-level and database-level) |
| `CleaningRule.java` | `rules/` | Interface for all cleaning rules (`apply(Booking b)`) |
| `application.properties` | `src/main/resources/` | Input path, output paths, DB settings |
| `log4j2.xml` | `src/main/resources/` | Logging configuration (console + file) |

**Deliverable:** Any new rule or component depends on Part 1; no cleaning logic here.

---

## Part 2 — Input/Output (Read & write)

**Purpose:** Reading raw CSV and writing cleaned, rejected, and aggregation CSVs.

| File | Location | Role |
|------|----------|------|
| `BookingReader.java` | `repository/` | Interface: `List<Booking> readAll(String path)` |
| `BookingWriter.java` | `repository/` | Interface: `writeCleaned`, `writeRejected`, `writeAggregation` |
| `CsvBookingReader.java` | `repository/` | Reads CSV, parses rows into `Booking`, handles headers |
| `CsvBookingWriter.java` | `repository/` | Writes cleaned.csv, rejected.csv, aggregation.csv with correct headers |

**Deliverable:** All file I/O for the pipeline; no business rules.

---

## Part 3 — Cleaning rules: normalization & validation (first batch)

**Purpose:** Three rules that normalize and validate core fields (names, numbers, dates).

| File | Location | Role |
|------|----------|------|
| `NameNormalizationRule.java` | `rules/` | Trim and proper-case passenger name; invalid → reject |
| `NumericValidationRule.java` | `rules/` | Parse and validate age (range) and fare; invalid → reject |
| `DateStandardizationRule.java` | `rules/` | Parse and standardize travel date to `yyyy-MM-dd`; invalid → reject |

**Deliverable:** Rules 1–3 from the use-case list (duplicates handled in Part 5).

---

## Part 4 — Cleaning rules: mapping, derivation & pipeline (second batch)

**Purpose:** Status/code normalization, derived fields, and the pipeline that runs all rules.

| File | Location | Role |
|------|----------|------|
| `StatusNormalizationRule.java` | `rules/` | Map status to CONFIRMED/CANCELLED (or reject) |
| `CodeMappingRule.java` | `rules/` | Map route code → route name (e.g. R1 → "Mumbai–Pune") |
| `DerivedFieldsRule.java` | `rules/` | Compute age category (Minor/Adult/Senior) and fare category (Low/Medium/High) |
| `CleaningPipeline.java` | `service/` | Holds list of `CleaningRule`; `process(Booking b)` applies each rule in order |

**Deliverable:** Rules 4–8 plus the pipeline orchestrator; valid/invalid split is in `Application`.

---

## Part 5 — Services & orchestration (dedup, aggregation, schema, main flow)

**Purpose:** Deduplication, route-wise aggregation, DB schema bootstrap, and main application flow.

| File | Location | Role |
|------|----------|------|
| `DuplicateService.java` | `service/` | Remove duplicates by booking ID (Set-based); valid list only |
| `AggregationService.java` | `service/` | Group by route; compute total bookings, revenue, confirmed/cancelled counts |
| `SchemaInitializer.java` | `repository/` | Ensure database exists (`CREATE DATABASE IF NOT EXISTS`) |
| `Application.java` | `app/` | Main: load config → read CSV → run pipeline → valid/invalid split → dedup → DB (if enabled) → write CSVs |

**Deliverable:** End-to-end flow, deduplication (rule 1 in use-case terms), aggregation (rule 9), and DB schema setup.

---

## Part 6 — Persistence & build (database + Maven)

**Purpose:** Saving cleaned bookings and aggregation rows to MySQL; build and deploy configuration.

| File | Location | Role |
|------|----------|------|
| `DbBookingRepository.java` | `repository/` | Insert/upsert cleaned bookings into `booking` table |
| `AggregationDbRepository.java` | `repository/` | Insert/upsert aggregation rows into `aggregation` table |
| `pom.xml` | project root | Maven build, dependencies (Log4j2, MySQL), Nexus distributionManagement, Sonar props |

**Deliverable:** All database writes and release/build setup (rule 10 “data categorization” is reflected in derived fields from Part 4).

---

## Flow across the 6 parts

```
Part 1 (config + model)
    ↓
Part 2 (read CSV)  →  Part 3 + Part 4 (cleaning pipeline + rules)
    ↓
Part 5 (dedup + aggregation + Application orchestration)
    ↓
Part 2 (write CSVs)  +  Part 6 (DB + build)
```

---

## Equality note

- **By responsibility:** Each part owns one major area (foundation, I/O, two rule batches, orchestration, persistence).
- **By file count:** Parts 1–2 and 4–6 have 4 files each; Part 3 has 3 (one part with 3 rules). Resources (properties, log4j2) are counted in Part 1.
- **By lines:** Parts 1–4 are similar in size (~130–165 lines); Parts 5 and 6 are larger (~261 and ~296) because they contain orchestration and all DB/build code. To make line counts closer, Part 5 and Part 6 could be subdivided only by splitting classes (e.g. extracting “DB bootstrap” or “Nexus/Sonar config”), which would change the current structure.

This division gives **exactly 6 parts**, with **nothing left out** and **no overlap**: every project file is assigned to exactly one part.
