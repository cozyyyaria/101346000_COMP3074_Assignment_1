package ca.gbc.comp3074.assignment1;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Requirement: "A back button should appear in the ActionBar of the AboutActivity
        // that takes user back to the MainActivity."
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.title_about);
        }

        // On-screen back button also finishes this activity and returns to existing MainActivity
        MaterialButton btnBackToMain = findViewById(R.id.btnBackToMain);
        if (btnBackToMain != null) {
            btnBackToMain.setOnClickListener(v -> finish());
        }
    }

    /**
     * Handles clicks on ActionBar items.
     * When android.R.id.home (the ActionBar back arrow) is clicked, finish() is called.
     *
     * Requirement: "Make sure that you do not start new instance of the MainActivity."
     * Calling finish() terminates AboutActivity and pops it off the Task's back stack,
     * immediately revealing the already-existing instance of MainActivity underneath
     * with all inputs and calculation results fully preserved.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Fallback for support action bar navigate up
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

