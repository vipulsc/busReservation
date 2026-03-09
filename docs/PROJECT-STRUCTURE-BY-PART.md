# Project Structure by Part (Part 1 – Part 6)

This document maps the **bus-cleaning** project structure to the six topic-based parts and their owners.

---

## Part 1: Application, Configuration & Data Model  
**Owner: AMAN**  
**Topic:** Entry point, configuration, and core `Booking` model.

```
bus-cleaning/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/bus/cleaning/
│   │   │   ├── app/
│   │   │   │   └── Application.java              # Main entry; orchestrates read → clean → dedupe → persist → write
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java                 # Paths and flags (input, output, DB on/off)
│   │   │   │   └── DbConfig.java                  # DB connection settings
│   │   │   └── model/
│   │   │       └── Booking.java                   # Core model: raw/cleaned fields, valid/invalidReason
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/bus/cleaning/
│           └── model/
│               └── BookingTest.java               # Unit tests for Booking (CSV row output, rejection reasons)
├── data/                                          # Put raw_bookings.csv here
└── output/                                        # Generated CSVs
```

---

## Part 2: Cleaning Pipeline & Validation Rules  
**Owner: NEHA**  
**Topic:** Pipeline orchestration and rules that validate/normalize names and numbers.

```
src/
├── main/java/com/bus/cleaning/
│   ├── rules/
│   │   ├── CleaningRule.java                      # Interface: void apply(Booking b)
│   │   ├── NameNormalizationRule.java             # Trim, proper case, letters/spaces only
│   │   └── NumericValidationRule.java            # Age 0–100, fare ≥ 0, parse checks
│   └── service/
│       └── CleaningPipeline.java                  # Runs a list of rules in sequence
└── test/java/com/bus/cleaning/
    ├── rules/
    │   ├── NameNormalizationRuleTest.java
    │   └── NumericValidationRuleTest.java
    └── service/
        └── CleaningPipelineTest.java
```

---

## Part 3: Standardization & Mapping Rules  
**Owner: SOUVIK**  
**Topic:** Standardize dates/status and map route codes to names.

```
src/
├── main/java/com/bus/cleaning/rules/
│   ├── DateStandardizationRule.java              # Multiple date formats → yyyy-MM-dd
│               # CONFIRMED/CNF/CONF → CONFIRMED; CANCELLED/CANCEL → CANCELLED
│   └── CodeMappingRule.java                      # Route codes (RT1–RT6) → route names
└── test/java/com/bus/cleaning/rules/
    ├── DateStandardizationRuleTest.java
    ├── StatusNormalizationRuleTest.java
    └── CodeMappingRuleTest.java
```

---

## Part 4: Ingestion, Derived Data & Writer Contract  
**Owner: VIPUL**  
**Topic:** Reading CSV, deriving age/fare categories, writer interface, and DB schema.

```
src/
├── main/java/com/bus/cleaning/
│   ├── rules/
│   │   └── DerivedFieldsRule.java                # Age category (Minor/Adult/Senior), fare category (Low/Medium/High)
│   └── repository/
│       ├── BookingReader.java                    # Interface: read all bookings
│       ├── CsvBookingReader.java                 # CSV implementation of reader
│       ├── BookingWriter.java                    # Interface: write cleaned, rejected, aggregation
│       └── SchemaInitializer.java               # DB schema creation (tables for bookings & aggregation)
└── test/java/com/bus/cleaning/
    ├── rules/
    │   └── DerivedFieldsRuleTest.java
    └── repository/
        └── CsvBookingReaderTest.java
```

---

## Part 5: File & Database Persistence  
**Owner: PIYUSH**  
**Topic:** Writing cleaned/rejected/aggregation CSVs and persisting bookings to MySQL.

```
src/
├── main/java/com/bus/cleaning/repository/
│   ├── CsvBookingWriter.java                     # Writes cleaned.csv, rejected.csv, aggregation.csv
│   └── DbBookingRepository.java                  # Saves cleaned bookings to MySQL
└── test/java/com/bus/cleaning/repository/
    └── CsvBookingWriterTest.java
```

---

## Part 6: Deduplication & Aggregation Services  
**Owner: SONAL**  
**Topic:** Removing duplicates and building route-wise aggregation (and persisting it).

```
src/
├── main/java/com/bus/cleaning/
    ├── StatusNormalizationRule.java  
│   ├── repository/
│   │   └── AggregationDbRepository.java          # Saves route-wise aggregation to MySQL
│   └── service/
│       ├── DuplicateService.java                 # Dedupe by booking ID or composite key
│       └── AggregationService.java               # Builds route-wise summary (count, total fare, etc.)
└── test/java/com/bus/cleaning/service/
    ├── DuplicateServiceTest.java
    └── AggregationServiceTest.java
```

---

## Summary Table

| Part | Owner  | Topic                                      | Key files |
|------|--------|--------------------------------------------|-----------|
| **1** | AMAN   | Application, config & data model           | Application.java, AppConfig.java, DbConfig.java, Booking.java, BookingTest.java |
| **2** | NEHA   | Cleaning pipeline & validation rules       | CleaningRule.java, CleaningPipeline.java, NameNormalizationRule.java, NumericValidationRule.java + tests |
| **3** | SOUVIK | Standardization & mapping rules            | DateStandardizationRule.java, StatusNormalizationRule.java, CodeMappingRule.java + tests |
| **4** | VIPUL  | Ingestion, derived data & writer contract  | DerivedFieldsRule.java, BookingReader.java, CsvBookingReader.java, BookingWriter.java, SchemaInitializer.java + tests |
| **5** | PIYUSH | File & database persistence                | CsvBookingWriter.java, DbBookingRepository.java + CsvBookingWriterTest.java |
| **6** | SONAL  | Deduplication & aggregation services       | DuplicateService.java, AggregationService.java, AggregationDbRepository.java + tests |

---

## Full Tree (Reference)

Single tree view of all parts combined:

```
bus-cleaning/
├── pom.xml
├── README.md
├── docs/
│   ├── PROJECT-DIVISION.md
│   ├── PROJECT-STRUCTURE-BY-PART.md    # this file
│   ├── part-1-AMAN.md … part-6-SONAL.md
│   └── …
├── src/
│   ├── main/
│   │   ├── java/com/bus/cleaning/
│   │   │   ├── app/Application.java
│   │   │   ├── config/AppConfig.java, DbConfig.java
│   │   │   ├── model/Booking.java
│   │   │   ├── repository/
│   │   │   │   ├── BookingReader.java, BookingWriter.java
│   │   │   │   ├── CsvBookingReader.java, CsvBookingWriter.java
│   │   │   │   ├── DbBookingRepository.java, AggregationDbRepository.java
│   │   │   │   └── SchemaInitializer.java
│   │   │   ├── rules/
│   │   │   │   ├── CleaningRule.java
│   │   │   │   ├── NameNormalizationRule.java, NumericValidationRule.java
│   │   │   │   ├── DateStandardizationRule.java, StatusNormalizationRule.java
│   │   │   │   ├── CodeMappingRule.java, DerivedFieldsRule.java
│   │   │   └── service/
│   │   │       ├── CleaningPipeline.java, DuplicateService.java, AggregationService.java
│   │   └── resources/application.properties
│   └── test/java/com/bus/cleaning/
│       ├── model/BookingTest.java
│       ├── rules/*Test.java
│       ├── service/CleaningPipelineTest.java, DuplicateServiceTest.java, AggregationServiceTest.java
│       └── repository/CsvBookingReaderTest.java, CsvBookingWriterTest.java
├── data/
└── output/
```
