# Capgemini Sprint – Bus Reservations | 5-Slide PPT Content

Based on *Bus_Full.docx* (Data Cleaning & Transformation Use Cases). Copy each section into one slide. Use the flow diagram page as Slide 3 (or 4); you can redraw the diagram in PowerPoint using shapes or paste the Mermaid output as image (see note below).

---

## Capgemini branding (colors, font, logo)

Use these for a consistent, on-brand PPT.

### Colors

| Use for | Name | HEX | RGB |
|--------|------|-----|-----|
| **Primary** (headers, key elements, logo) | Capgemini Blue | **#0070AD** | 0, 112, 173 |
| **Accent** (highlights, links, diagrams) | Vibrant Blue | **#17ABDA** | 23, 171, 218 |

**In PowerPoint:**  
- Set theme colors: Primary = `#0070AD`, Accent = `#17ABDA`.  
- Use Capgemini Blue for titles and main shapes; Vibrant Blue for bullets, arrows, and accents.  
- Background: white `#FFFFFF` or very light grey. Text: dark grey/black for body.

### Font

- **Titles / headings:** **Arial Bold** or **Arial Black** (clean, corporate; matches common Capgemini decks).  
- **Body / bullets:** **Arial** or **Arial Regular**, 18–24 pt for body, 24–32 pt for titles.  
- Official guidelines reference a custom wordmark for the logo; for slide text, Arial is the safe, widely used choice in Capgemini materials.  
- If your team has a brand pack, prefer the font specified there (e.g. a custom sans-serif).

### Logo

- **Official source:** [Capgemini Visual Identity](https://visualidentity.capgemini.com) (login may be required for full asset library).  
- **For quick use:** Download PNG/SVG from [SeekLogo – Capgemini](https://seeklogo.com/vector-logo/25892/capgemini) or [BrandLogoVector – Capgemini](https://brandlogovector.com/capgemini-logo-vector/).  
- **Placement:** Top-left or top-right of title slide; smaller on subsequent slides (e.g. corner).  
- **Versions:** Use **blue logo** on white/light slides; use **white logo** on blue (`#0070AD`) backgrounds.

---

## SLIDE 1 — Title & Problem

**Title:** Bus Reservations – Data Cleaning & Transformation

**Subtitle:** Transforming raw reservation data into reliable, actionable insights

**Problem (2 bullets):**
- Raw bus reservation data has inconsistent names, dates, statuses, codes, and invalid or duplicate records
- Need a reliable Core Java pipeline to clean, validate, transform, and load data for reporting and DB

**Execution in one line:**  
End-to-end pipeline: CSV in → clean, transform, deduplicate → cleaned + rejected + aggregation out (+ optional MySQL).

**Visual:** Use one strong image (bus/transport + data or dashboard).

---

## SLIDE 2 — Solution Delivered / What We Built

**Title (choose one):**
- **Solution Delivered** *(recommended — professional)*
- **What We Delivered**
- **Our Deliverable**

**Opening paragraph (2–3 sentences):**

We delivered an end-to-end **Bus Reservations Data Pipeline** in Core Java that ingests raw CSV data, runs it through a configurable cleaning and transformation engine, and produces cleaned datasets alongside rejected records and aggregations—with optional persistence to MySQL. The solution is designed for reliability and auditability: every invalid record is flagged and written to a separate file, while valid data flows into cleaned outputs and the database. The pipeline implements **10 cleaning and transformation rules**, each delivered as a focused, testable component.

---

**Delivered components — 10 rules (one line each):**

1. **Remove Duplicates** — Unique records by ID (Set-based).
2. **Normalize Names** — Trim and proper case.
3. **Fix Numeric Fields** — Parse, validate, reject invalid.
4. **Standardize Dates** — Single format (yyyy-MM-dd).
5. **Map Codes** — Short codes → full labels (Map lookup).
6. **Validate Status** — Normalize to fixed set (e.g. CONFIRMED, CANCELLED).
7. **Flag Invalid Records** — Log and output to rejected list.
8. **Derived Fields** — New columns from existing data.
9. **Aggregation** — Group by route/date; summary metrics.
10. **Data Categorization** — Assign to category buckets.

**Visual:** Screenshot of output files (cleaned.csv, rejected.csv, aggregation.csv) or a sample of transformed data.

---

## SLIDE 3 — Full Process Flow Diagram

**Title:** End-to-End Process Flow

**Use the diagram below in one of these ways:**
1. **PowerPoint:** Redraw using shapes (rectangles for steps, arrows between them).
2. **Image:** Go to [mermaid.live](https://mermaid.live), paste the Mermaid code, export as PNG/SVG, insert in PPT.

---

### Option A – Mermaid (for image export)

```mermaid
flowchart LR
    subgraph INPUT
        A[📄 Raw CSV]
    end

    subgraph PIPELINE
        B[Read All Records]
        C[Cleaning Pipeline]
        D[Valid / Invalid Split]
        E[Remove Duplicates]
    end

    subgraph RULES
        R1[Duplicates]
        R2[Names]
        R3[Numeric]
        R4[Dates]
        R5[Codes]
        R6[Status]
        R7[Invalid Flag]
        R8[Derived]
        R9[Aggregation]
        R10[Categorize]
    end

    subgraph OUTPUT
        F[(MySQL: Booking + Aggregation)]
        G[Cleaned CSV]
        H[Rejected CSV]
        I[Aggregation CSV]
    end

    A --> B
    B --> C
    C --> R1 --> R2 --> R3 --> R4 --> R5 --> R6 --> R7 --> R8 --> R9 --> R10
    R10 --> D
    D --> E
    E --> F
    E --> G
    D --> H
    E --> I
```

---

### Option B – Text flow (copy-paste into PPT as-is)

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────────────────────┐
│  RAW CSV    │────▶│  READ ALL         │────▶│  CLEANING PIPELINE          │
│  (Input)    │     │  RECORDS          │     │  • Name normalization       │
└─────────────┘     └──────────────────┘     │  • Numeric validation       │
                                             │  • Date standardization     │
                                             │  • Status normalization     │
                                             │  • Code mapping             │
                                             │  • Derived fields           │
                                             └──────────────┬──────────────┘
                                                            │
                                                            ▼
┌─────────────┐     ┌──────────────────┐     ┌─────────────────────────────┐
│ AGGREGATION │◀────│  MYSQL (if on)   │◀────│  REMOVE DUPLICATES          │
│ CSV         │     │  • Booking table  │     │  (valid records only)       │
└─────────────┘     │  • Aggregation    │     └──────────────┬──────────────┘
                    └──────────────────┘                    │
┌─────────────┐     ┌──────────────────┐                    │
│ REJECTED    │◀────│  VALID / INVALID │◀────────────────────┘
│ CSV         │     │  SPLIT           │
└─────────────┘     └──────────────────┘
┌─────────────┐
│ CLEANED CSV │◀──── (unique valid records)
└─────────────┘
```

---

### Option C – Simple linear (for quick PPT shapes)

**Step 1** → **Step 2** → **Step 3** → **Step 4** → **Step 5**

| Step | Name                | What happens                                      |
|------|---------------------|---------------------------------------------------|
| 1    | Read                | Load all rows from input CSV                      |
| 2    | Cleaning pipeline   | Apply 10 rules; set valid/invalid per record     |
| 3    | Split               | Valid list vs rejected list                       |
| 4    | Deduplicate         | Remove duplicates from valid list                |
| 5    | Output              | Save to MySQL (if enabled) + cleaned/rejected/aggregation CSVs |

---

### Option D – One-line flow (paste into PPT, then draw boxes)

```
[Raw CSV] → [Read] → [Clean: 10 rules] → [Valid/Invalid] → [Deduplicate] → [MySQL + Cleaned CSV + Rejected CSV + Aggregation CSV]
```

**Box labels for PPT shapes:**  
1. Raw CSV  
2. Read records  
3. Cleaning pipeline (10 rules)  
4. Valid / Invalid split  
5. Deduplicate  
6. Output: DB + 3 CSVs  

---

## SLIDE 4 — Execution & Demo

**Title:** Execution – Run & Results

**Screenshot 1 – Build:**
- Terminal: `mvn clean package` (or `mvn clean compile`)

**Screenshot 2 – Run:**
- Terminal: Run command + log lines:
  - `BUS CLEANING JOB STARTED`
  - `Total records read: …`
  - `Valid before de-dup: …` / `Rejected records: …`
  - `Valid after de-dup: …`
  - `Database operations completed successfully` (if DB on)
  - `BUS CLEANING JOB COMPLETED SUCCESSFULLY`

**Screenshot 3 – Output:**
- File explorer: `cleaned.csv`, `rejected.csv`, `aggregation.csv` (or DB rows)

**Bullets:**
- Run: `java -jar …` or `mvn exec:java` (as per your setup)
- Input: X records → Y valid, Z rejected
- Output: 3 CSVs (+ DB when enabled)

---

## SLIDE 5 — DevOps

**Title:** DevOps & Release

**Bullets:**
- **Build:** Maven – `mvn clean package` / `mvn clean deploy`
- **Artifact repository:** Nexus – `maven-snapshots` (SNAPSHOT), `maven-bus` (release)
- **Quality:** SonarQube – project key `bus-cleaning`, analysis on build
- **Deploy:** `mvn clean deploy` → artifacts published to Nexus
- **Config:** `distributionManagement` in `pom.xml`; credentials in `~/.m2/settings.xml`
- **Optional:** Nexus run via Docker (`sonatype/nexus3` on port 8081)

**Visual:** Screenshot of Nexus (Browse → maven-snapshots / maven-bus) or SonarQube project dashboard.

---

## SLIDE 6 — Summary & Next (if you need 6th slide)

**Title:** Summary & Next Steps

**Done:**
- End-to-end Bus Reservations data pipeline: read → clean → transform → deduplicate → DB + CSV
- 10 cleaning & transformation rules (per use-case document); duplicate handling; aggregation
- DevOps: Maven build, Nexus artifact repository, SonarQube integration

**Next (optional):**
- CI pipeline (e.g. GitHub Actions): build → test → SonarQube → deploy to Nexus
- Docker image for the application

**Visual:** Team/sprint photo or “Thank you”.

---

## Diagram note for PPT

- **Full process diagram:** Use **Slide 3** content; pick Option A (Mermaid → export image), Option B (text flow), or Option C (table + shapes).
- For **Mermaid:** Copy the code from Option A into [mermaid.live](https://mermaid.live) → Export as PNG or SVG → Insert image in PPT.
