# COMP3074 - Assignment 1: Payroll Calculator Application

**Course:** COMP3074 - Mobile Application Development I (Fall 2026)  
**Student Name:** Ariana Cruz  
**Student ID:** 101346000  
**Language:** Java  
**IDE:** Android Studio  

---

## 📌 Project Overview
This Android application calculates an employee's weekly payroll, including regular pay, overtime pay (at 1.5x for hours worked beyond 40), and tax deductions. The application also includes an **About** activity showcasing student identity and course metadata, with proper back-stack navigation handling.

---

## ✨ Features & Rubric Requirements Fulfilled

1. **MainActivity (Input, Calculation & Output Implementation - 6 Points):**
   - **Inputs:**
     - Number of hours worked (`TextInputEditText` with `numberDecimal`)
     - Hourly rate (`TextInputEditText` with `numberDecimal`)
     - Tax rate (`TextInputEditText` with `numberDecimal`, accepting either decimal `0.13` or percentage `13`)
   - **Formulas:**
     - If $\text{hours} \le 40$:
       $$\text{pay} = \text{hours} \times \text{rate}$$
       $$\text{overtime\_pay} = 0$$
     - If $\text{hours} > 40$:
       $$\text{pay} = 40 \times \text{rate}$$
       $$\text{overtime\_pay} = (\text{hours} - 40) \times \text{rate} \times 1.5$$
     - Total Pay:
       $$\text{total\_pay} = \text{pay} + \text{overtime\_pay}$$
     - Tax:
       $$\text{tax} = \text{pay} \times \text{tax\_rate}$$
   - **Outputs:**
     - Formatted Canadian currency (`$#,##0.00`) for Regular Pay, Overtime Pay, Total Pay, and Tax.
   - **UX & Robustness:**
     - Input validation using Material `TextInputLayout` error states and Toast notices.
     - Screen rotation state preservation (`onSaveInstanceState` and `onRestoreInstanceState`).
     - "Clear" button to quickly reset fields and reset focus.

2. **AboutActivity (2 Points):**
   - Displays full name, student ID, course details, and institution.
   - Accessible via both the **ActionBar Menu Item** (top right) and the on-screen **"About Me"** button.

3. **Navigation Implementation (2 Points):**
   - "A back button should appear in the ActionBar of the AboutActivity that takes user back to the MainActivity. Make sure that you do not start new instance of the MainActivity."
   - Implemented via `getSupportActionBar().setDisplayHomeAsUpEnabled(true)` and handling `android.R.id.home` with `finish()`.
   - `finish()` pops `AboutActivity` from the task back stack, instantly revealing the already-existing instance of `MainActivity` with all previous inputs and calculations preserved.

4. **AI Usage Declaration (aud.pdf):**
   - Completed and generated as `aud.pdf` in the project root directory, conforming with George Brown College academic integrity requirements.

---

## 🚀 How to Run in Android Studio

1. Open **Android Studio**.
2. Select **File > Open...** and browse to this directory (`COMP3074/assignment1`).
3. Allow Gradle to sync dependencies.
4. Select an Android Emulator or physical device (Android 7.0 / API 24 or newer).
5. Click the green **Run (▶)** button or press `Shift + F10`.

---

## 🎓 Interview Study Guide (Code Grade × Interview)

During the interview, the professor will ask 3 questions about your code. Below are key questions and answers you should study:

### Q1: How did you ensure the ActionBar back button does not create a new instance of MainActivity?
> **Answer:**
> "In `AboutActivity.java`, we enabled the Up arrow using `getSupportActionBar().setDisplayHomeAsUpEnabled(true)`. In `onOptionsItemSelected(MenuItem item)`, when `item.getItemId() == android.R.id.home`, we call `finish()`.
> In Android, `MainActivity` started `AboutActivity`, so `AboutActivity` sits on top of the Task's back stack (`[MainActivity, AboutActivity]`). Calling `finish()` destroys `AboutActivity` and pops it off the stack, bringing the existing `MainActivity` back to the foreground without restarting or recreating it. If we had instead called `startActivity(new Intent(this, MainActivity.class))`, it would have pushed a duplicate instance of `MainActivity` onto the stack and cleared the user's data."

### Q2: Explain the payroll formula and how you handled overtime and tax.
> **Answer:**
> "We check if the entered hours are $\le 40$ or $> 40$. If $\le 40$, regular pay is `hours * rate` and overtime pay is `0.0`. If $> 40$, regular pay is capped at `40 * rate`, and overtime pay is calculated for the surplus hours `(hours - 40) * rate * 1.5`. Total pay is the sum of both.
> For tax, the assignment requires `tax = pay * tax_rate`. We also normalize the tax rate input: if a user types `13` instead of `0.13`, we divide by 100 so calculations remain mathematically accurate."

### Q3: How do you handle input validation and prevent crashes?
> **Answer:**
> "We check for empty inputs using `TextUtils.isEmpty()` before parsing. If any field is empty or contains negative numbers, we display inline error hints via `TextInputLayout.setError()` and inform the user with a `Toast`. We wrap string-to-double conversion in a `try-catch` block catching `NumberFormatException` to guarantee the app never crashes from invalid numeric formatting."

