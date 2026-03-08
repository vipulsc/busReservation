# Part 6: Deduplication & Aggregation Services  
**For Scrum Master & Stakeholders – Short Intro**

Hello / Good afternoon / Good evening. I am **[Your Name]**.

---

## What I Own

I own **removing duplicates** from valid bookings and **building route-wise aggregation** (and persisting it to the DB) — so we have a single set of unique bookings and summary reports by route.

---

## What I Did

1. **DuplicateService**  
   After cleaning, the same booking can appear more than once. I deduplicate by **booking ID** or by a composite key (e.g. passenger name, bus, route, date). Only one record per duplicate set is kept so downstream and DB don’t get duplicate rows.

2. **AggregationService**  
   I build a **route-wise summary** from the cleaned, deduplicated bookings: for each route we have total bookings, total revenue (sum of fare), and counts of confirmed vs cancelled. The list is sorted by revenue so the most valuable routes are on top. This feeds both the aggregation CSV and the aggregation report.

3. **AggregationDbRepository**  
   When the database is enabled, I persist the aggregation rows to MySQL (the aggregation table from the schema). So route summaries are available in the DB for dashboards or other tools.

4. **Tests**  
   I wrote unit tests for deduplication logic and for aggregation (grouping, totals, sorting).

---

## One-Liner for the Meet

*"I own deduplicating valid bookings so we don’t double-count, building route-wise aggregation (bookings, revenue, confirmed/cancelled), and saving that aggregation to the database."*

---

**Files:** `DuplicateService.java`, `AggregationService.java`, `AggregationDbRepository.java`, plus their tests.

That’s all from my side. Thank you.
