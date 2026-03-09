# Part 3: Standardization & Mapping Rules  
**For Scrum Master & Stakeholders – Short Intro**

Hello / Good afternoon / Good evening. I am **[SOUVIK]**.

---

## What I Own

I own the rules that **standardize** dates and status and **map** route codes to names — so the data is consistent and business-friendly (e.g. one date format, one status wording, readable route names).

---

## What I Did

1. **DateStandardizationRule**  
   Raw data has dates in many formats (e.g. 17/04/24, 09-04-2024, 2024/04/14). I normalize all of them to a single format: **yyyy-MM-dd**, and store it in `travelDateStd`. Unparseable dates mark the booking invalid.

2. **StatusNormalizationRule**  
   Status comes in as CONF, Confirm, confirmed, CANCELLED, Cancelled, etc. I map them to exactly **CONFIRMED** or **CANCELLED** so downstream logic and reports use a single set of values.

3. **CodeMappingRule**  
   Route codes like RT1, RT2, … RT6 are mapped to human-readable route names (e.g. Mumbai–Pune, Delhi–Agra). I fill `routeName` on the booking so aggregation and reports show route names instead of codes.

4. **Tests**  
   I wrote unit tests for date standardization, status normalization, and code mapping so we can change formats or mappings safely.

---

## One-Liner for the Meet

*"I implemented date standardization to one format, status normalization to CONFIRMED/CANCELLED, and route-code-to-name mapping so the data is consistent and readable for reports."*

---

**Files:** `DateStandardizationRule.java`, `StatusNormalizationRule.java`, `CodeMappingRule.java`, plus their tests.

That’s all from my side. Thank you.
