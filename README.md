# Bus Reservation – Data Cleaning Pipeline

A Java application that reads raw bus booking data from CSV, validates and cleans it through a configurable pipeline, removes duplicates, and outputs cleaned records, rejected records, and route-wise aggregation. Optionally persists results to MySQL.

---

## Features

- **CSV ingestion** – Read bookings from a CSV file (configurable path).
- **Cleaning pipeline** – Six rules run in sequence:
  - **Name normalization** – Trim, proper case, letters/spaces only.
  - **Numeric validation** – Age (0–100), fare (non-negative), parse checks.
  - **Date standardization** – Multiple input formats → `yyyy-MM-dd`.
  - **Status normalization** – CONFIRMED/CNF/CONF → CONFIRMED; CANCELLED/CANCEL → CANCELLED.
  - **Code mapping** – Route codes (e.g. RT1–RT6) mapped to route names.
  - **Derived fields** – Age category (Minor/Adult/Senior), fare category (Low/Medium/High).
- **Duplicate removal** – By booking ID, or by composite key (name, bus, route, date) when ID is missing.
- **Outputs** – Cleaned CSV, rejected CSV (with reasons), and aggregation CSV (route-wise summary).
- **Optional database** – Store cleaned bookings and aggregation in MySQL when enabled.
- **Tests** – JUnit 5 unit tests for model, rules, services, and CSV read/write.

---

## Prerequisites

- **Java 8** or higher  
- **Maven 3.6+**  
- (Optional) **MySQL 8** if you enable database persistence  

---

## Project Structure

```
busReservation/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/bus/cleaning/
│   │   │   ├── app/Application.java          # Entry point
│   │   │   ├── config/AppConfig.java, DbConfig.java
│   │   │   ├── model/Booking.java
│   │   │   ├── repository/                   # CSV & DB read/write
│   │   │   │   ├── BookingReader.java, BookingWriter.java
│   │   │   │   ├── CsvBookingReader.java, CsvBookingWriter.java
│   │   │   │   ├── DbBookingRepository.java, AggregationDbRepository.java
│   │   │   │   └── SchemaInitializer.java
│   │   │   ├── rules/                        # Cleaning rules
│   │   │   │   ├── CleaningRule.java
│   │   │   │   ├── NameNormalizationRule.java, NumericValidationRule.java
│   │   │   │   ├── DateStandardizationRule.java, StatusNormalizationRule.java
│   │   │   │   ├── CodeMappingRule.java, DerivedFieldsRule.java
│   │   │   └── service/
│   │   │       ├── CleaningPipeline.java, DuplicateService.java
│   │   │       └── AggregationService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/bus/cleaning/            # JUnit 5 tests
│           ├── model/BookingTest.java
│           ├── rules/*Test.java
│           ├── service/*Test.java
│           └── repository/CsvBookingReaderTest.java, CsvBookingWriterTest.java
├── data/                                     # Put raw_bookings.csv here
└── output/                                   # Generated CSVs
```

---

## Configuration

Edit `src/main/resources/application.properties`:

| Property | Description | Example |
|----------|-------------|---------|
| `input.path` | Path to input CSV | `data/raw_bookings.csv` |
| `output.cleaned` | Path for cleaned records | `output/cleaned.csv` |
| `output.rejected` | Path for rejected records (with reason) | `output/rejected.csv` |
| `output.aggregation` | Path for route-wise summary | `output/aggregation.csv` |
| `db.enabled` | Use MySQL for persistence | `true` or `false` |
| `db.url` | JDBC URL | `jdbc:mysql://host:port/dbname` |
| `db.username` | Database user | `root` |
| `db.password` | Database password | (your password) |

Example (database disabled):

```properties
input.path=data/raw_bookings.csv
output.cleaned=output/cleaned.csv
output.rejected=output/rejected.csv
output.aggregation=output/aggregation.csv
db.enabled=false
```

---

## Input CSV Format

The input file must have a header row and at least 9 columns per data row. Rows with fewer than 9 columns are skipped.

| Column index | Field | Description |
|--------------|--------|-------------|
| 0 | BookingID | Unique or empty |
| 1 | PassengerName | Letters and spaces (normalized to proper case) |
| 2 | Age | Integer 0–100 |
| 3 | Gender | Any (passed through) |
| 4 | BusCode | Any (passed through) |
| 5 | RouteCode | e.g. RT1–RT6 (mapped to route names) |
| 6 | TravelDate | dd/MM/yy, dd-MM-yyyy, yyyy/MM/dd, or yyyy-MM-dd |
| 7 | Fare | Non-negative number |
| 8 | Status | CONFIRMED/CNF/CONF or CANCELLED/CANCEL/CANCELED |

Example header and one row:

```csv
BookingID,PassengerName,Age,Gender,BusCode,RouteCode,TravelDate,Fare,Status
B001,john doe,25,M,BUS1,RT1,15/06/24,500,CONFIRMED
```

---

## Build and Run

**Build:**

```bash
mvn clean compile
```

**Run (from project root):**

```bash
mvn exec:java -Dexec.mainClass="com.bus.cleaning.app.Application"
```

Or run the `Application` main class from your IDE. Ensure `data/raw_bookings.csv` exists (or set `input.path` to your file).

**Run tests:**

```bash
mvn test
```

### Test folder not visible in IntelliJ or Eclipse

If `src/test/java` does not show up or is not recognized as test source:

**IntelliJ IDEA**
1. Right-click `pom.xml` → **Maven** → **Reload Project** (or use the Maven tool window refresh).
2. If `src/test/java` still doesn’t appear: right-click `src/test/java` → **Mark Directory as** → **Test Sources Root** (folder should turn green).
3. **File** → **Invalidate Caches / Restart** if the folder still doesn’t appear.

**Eclipse**
1. Right-click the project → **Maven** → **Update Project** (Alt+F5). Check **Force Update** if needed → **OK**.
2. If the test folder is still missing: right-click project → **Build Path** → **Configure Build Path** → **Source** tab → **Add Folder** → select `src/test/java` → check **Apply and Close**. Ensure the folder is marked as a test source (e.g. "Test" in the list).
3. Refresh the project: right-click project → **Refresh** (F5).

The `pom.xml` defines `testSourceDirectory` as `src/test/java`, so a Maven-based import should pick it up after reload/update.

---

## Output Files

- **Cleaned CSV** – Valid records after cleaning and de-duplication. Columns include original fields plus RouteName, AgeCategory, FareCategory.
- **Rejected CSV** – Invalid records with an extra `Reason` column (e.g. InvalidAgeFormat, MissingName, InvalidStatus).
- **Aggregation CSV** – Route-wise summary: RouteName, TotalBookings, TotalRevenue, ConfirmedCount, CancelledCount (sorted by revenue descending).

---

## Route Code Mapping

| Code | Route name |
|------|------------|
| RT1 | Delhi-Jaipur |
| RT2 | Mumbai-Pune |
| RT3 | Bangalore-Chennai |
| RT4 | Hyderabad-Goa |
| RT5 | Kolkata-Patna |
| RT6 | Ahmedabad-Surat |

Unknown codes produce a rejection reason `UnknownRouteCode`.

---

## SonarQube (Code Quality)

Code analysis is configured in `pom.xml` (`sonar.projectKey`, `sonar.host.url`). Run SonarQube via Docker, then execute the analysis **from the project root directory**.

**Start SonarQube (Docker):**

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts
```

- **URL:** http://localhost:9000 (default login: `admin` / `admin`; you’ll be prompted to change the password on first use).

**Run analysis (must be run from the project directory, e.g. `busReservation/`):**

```bash
cd /path/to/busReservation
mvn clean test sonar:sonar
```

Results are sent to the SonarQube server. Open http://localhost:9000 to view the project dashboard.

---

## Nexus (Artifact Repository)

The project’s `pom.xml` includes `distributionManagement` for deploying artifacts to Nexus (releases and snapshots). Run Nexus via Docker and use the generated admin password for first login.

**Start Nexus (Docker):**

```bash
docker run -d --name nexus -p 8081:8081 sonatype/nexus3
```

- **URL:** http://localhost:8081

**Get the initial admin password:**

```bash
docker exec -it nexus cat /nexus-data/admin.password
```

Use that password to log in as user `admin` at http://localhost:8081. You’ll be prompted to change it. The default repository URLs in `pom.xml` are:

- **Releases:** `http://localhost:8081/repository/maven-bus/`
- **Snapshots:** `http://localhost:8081/repository/maven-snapshots/`

Adjust these in `pom.xml` or in Nexus if your setup uses different hosts/ports or repository names.

---

## Tech Stack

- **Java 8**
- **Maven**
- **Log4j 2** – Logging
- **MySQL Connector/J 8** – Optional DB
- **JUnit 5** – Unit tests
- **SonarQube** – Code quality (optional; run via Docker)
- **Nexus 3** – Artifact repository (optional; run via Docker)

---

## License

This project is for internal/sprint use. Adjust license as needed for your organization.
