package ca.gbc.comp3074.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Input layout containers for inline error handling
    private TextInputLayout tilHoursWorked;
    private TextInputLayout tilHourlyRate;
    private TextInputLayout tilTaxRate;

    // Input fields
    private TextInputEditText etHoursWorked;
    private TextInputEditText etHourlyRate;
    private TextInputEditText etTaxRate;

    // Output views
    private TextView tvRegularPay;
    private TextView tvOvertimePay;
    private TextView tvTotalPay;
    private TextView tvTax;

    // Buttons
    private MaterialButton btnCalculate;
    private MaterialButton btnClear;
    private MaterialButton btnAbout;

    // Keys for saving instance state on configuration change (e.g. rotation)
    private static final String KEY_REGULAR_PAY = "key_regular_pay";
    private static final String KEY_OVERTIME_PAY = "key_overtime_pay";
    private static final String KEY_TOTAL_PAY = "key_total_pay";
    private static final String KEY_TAX = "key_tax";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize view references
        initializeViews();

        // Setup button click listeners
        setupListeners();

        // Restore calculated results if activity was recreated (e.g. rotation)
        if (savedInstanceState != null) {
            tvRegularPay.setText(savedInstanceState.getString(KEY_REGULAR_PAY, getString(R.string.default_amount)));
            tvOvertimePay.setText(savedInstanceState.getString(KEY_OVERTIME_PAY, getString(R.string.default_amount)));
            tvTotalPay.setText(savedInstanceState.getString(KEY_TOTAL_PAY, getString(R.string.default_amount)));
            tvTax.setText(savedInstanceState.getString(KEY_TAX, getString(R.string.default_amount)));
        }
    }

    /**
     * Finds and assigns all UI components from activity_main.xml
     */
    private void initializeViews() {
        tilHoursWorked = findViewById(R.id.tilHoursWorked);
        tilHourlyRate = findViewById(R.id.tilHourlyRate);
        tilTaxRate = findViewById(R.id.tilTaxRate);

        etHoursWorked = findViewById(R.id.etHoursWorked);
        etHourlyRate = findViewById(R.id.etHourlyRate);
        etTaxRate = findViewById(R.id.etTaxRate);

        tvRegularPay = findViewById(R.id.tvRegularPay);
        tvOvertimePay = findViewById(R.id.tvOvertimePay);
        tvTotalPay = findViewById(R.id.tvTotalPay);
        tvTax = findViewById(R.id.tvTax);

        btnCalculate = findViewById(R.id.btnCalculate);
        btnClear = findViewById(R.id.btnClear);
        btnAbout = findViewById(R.id.btnAbout);
    }

    /**
     * Attaches click listeners to action buttons
     */
    private void setupListeners() {
        btnCalculate.setOnClickListener(v -> calculatePayroll());
        btnClear.setOnClickListener(v -> clearAllFields());
        btnAbout.setOnClickListener(v -> openAboutActivity());
    }

    /**
     * Validates input values, calculates pay and tax according to course formulas,
     * and presents formatted currency output to the user.
     */
    private void calculatePayroll() {
        // Clear previous error highlights
        clearErrors();

        String hoursStr = etHoursWorked.getText() != null ? etHoursWorked.getText().toString().trim() : "";
        String rateStr = etHourlyRate.getText() != null ? etHourlyRate.getText().toString().trim() : "";
        String taxRateStr = etTaxRate.getText() != null ? etTaxRate.getText().toString().trim() : "";

        // Validate that no fields are left empty
        boolean hasError = false;

        if (TextUtils.isEmpty(hoursStr)) {
            tilHoursWorked.setError(getString(R.string.error_empty_hours));
            hasError = true;
        }

        if (TextUtils.isEmpty(rateStr)) {
            tilHourlyRate.setError(getString(R.string.error_empty_rate));
            hasError = true;
        }

        if (TextUtils.isEmpty(taxRateStr)) {
            tilTaxRate.setError(getString(R.string.error_empty_tax));
            hasError = true;
        }

        if (hasError) {
            Toast.makeText(this, R.string.error_invalid_input, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double hoursWorked = Double.parseDouble(hoursStr);
            double hourlyRate = Double.parseDouble(rateStr);
            double enteredTaxRate = Double.parseDouble(taxRateStr);

            // Validation: negative values are not allowed for work hours and rates
            if (hoursWorked < 0 || hourlyRate < 0 || enteredTaxRate < 0) {
                Toast.makeText(this, R.string.error_negative_values, Toast.LENGTH_LONG).show();
                if (hoursWorked < 0) tilHoursWorked.setError(getString(R.string.error_negative_values));
                if (hourlyRate < 0) tilHourlyRate.setError(getString(R.string.error_negative_values));
                if (enteredTaxRate < 0) tilTaxRate.setError(getString(R.string.error_negative_values));
                return;
            }

            // Normalize tax rate: if user inputs a percentage like 13 or 18, convert to decimal (0.13 or 0.18)
            double taxRate = (enteredTaxRate > 1.0) ? (enteredTaxRate / 100.0) : enteredTaxRate;

            // --- Assignment Formula Implementation ---
            // Formula requirement:
            // if no_of_hours <= 40 then
            //     pay = no_of_hours * hourly_rate, overtime_pay = 0
            // else
            //     pay = 40 * hourly_rate
            //     overtime_pay = (no_of_hours - 40) * hourly_rate * 1.5
            // total_pay = pay + overtime_pay
            // tax = pay * tax_rate

            double regularPay;
            double overtimePay;

            if (hoursWorked <= 40.0) {
                regularPay = hoursWorked * hourlyRate;
                overtimePay = 0.0;
            } else {
                regularPay = 40.0 * hourlyRate;
                overtimePay = (hoursWorked - 40.0) * hourlyRate * 1.5;
            }

            double totalPay = regularPay + overtimePay;

            // Assignment specification: tax = pay * tax_rate (applied to regular pay)
            double tax = regularPay * taxRate;

            // Format amounts as Canadian currency ($#,##0.00)
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.CANADA);

            tvRegularPay.setText(currencyFormat.format(regularPay));
            tvOvertimePay.setText(currencyFormat.format(overtimePay));
            tvTotalPay.setText(currencyFormat.format(totalPay));
            tvTax.setText(currencyFormat.format(tax));

        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_invalid_number, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clears all inputs and resets displayed outputs back to default
     */
    private void clearAllFields() {
        clearErrors();

        if (etHoursWorked.getText() != null) etHoursWorked.getText().clear();
        if (etHourlyRate.getText() != null) etHourlyRate.getText().clear();
        if (etTaxRate.getText() != null) etTaxRate.getText().clear();

        tvRegularPay.setText(R.string.default_amount);
        tvOvertimePay.setText(R.string.default_amount);
        tvTotalPay.setText(R.string.default_amount);
        tvTax.setText(R.string.default_amount);

        etHoursWorked.requestFocus();
    }

    /**
     * Removes any inline error text on input fields
     */
    private void clearErrors() {
        tilHoursWorked.setError(null);
        tilHourlyRate.setError(null);
        tilTaxRate.setError(null);
    }

    /**
     * Navigates to AboutActivity via an Explicit Intent.
     */
    private void openAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    // --- Options Menu (ActionBar) Implementation ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            openAboutActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- State Preservation across Screen Rotation ---

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_REGULAR_PAY, tvRegularPay.getText().toString());
        outState.putString(KEY_OVERTIME_PAY, tvOvertimePay.getText().toString());
        outState.putString(KEY_TOTAL_PAY, tvTotalPay.getText().toString());
        outState.putString(KEY_TAX, tvTax.getText().toString());
    }
}

