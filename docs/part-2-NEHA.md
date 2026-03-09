# Part 2: Cleaning Pipeline & Validation Rules  
**For Scrum Master & Stakeholders – Short Intro**

Hello / Good afternoon / Good evening. I am **[NEHA]**.

---

## What I Own

I own the **cleaning pipeline** and the rules that **validate and normalize** names and numbers — *how cleaning is run* and *how we catch invalid or messy data* for names and numerics.

---

## What I Did

1. **CleaningRule interface & CleaningPipeline**  
   I defined the `CleaningRule` interface with a single method `apply(Booking b)`. The **CleaningPipeline** holds a list of these rules and runs them one after another on each booking. So every record goes through the same sequence of rules.

2. **NameNormalizationRule**  
   I trim whitespace, enforce proper case (first letter capital per word), and allow only letters and spaces. Invalid names are rejected with a reason so they go to the rejected file.

3. **NumericValidationRule**  
   I validate **age** (must be 0–100, parseable number) and **fare** (must be ≥ 0, parseable number). Negative ages, negative fares, or non-numeric values mark the booking invalid and add a clear reason.

4. **Tests**  
   I wrote unit tests for name normalization, numeric validation, and for the pipeline running multiple rules in order.

---

## One-Liner for the Meet

*"I built the cleaning pipeline that runs all rules in sequence, and I implemented name normalization and numeric validation so invalid names and ages/fares are caught and rejected with a reason."*

---

**Files:** `CleaningRule.java`, `CleaningPipeline.java`, `NameNormalizationRule.java`, `NumericValidationRule.java`, plus their tests.

That’s all from my side. Thank you.
