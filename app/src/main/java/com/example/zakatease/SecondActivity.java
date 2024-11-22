package com.example.zakatease;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class SecondActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Variables for navigation
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    // Variables for Zakat calculation
    private EditText goldWeightInput, goldValueInput;
    private Spinner goldTypeSpinner;
    private Button calculateButton;

    private double uruf; // Uruf value based on gold type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second); // Ensure this layout includes Zakat calculator UI

        // Navigation Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.about_toolbar);

        // Tool Bar
        setSupportActionBar(toolbar);

        // Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Zakat calculator UI components
        goldWeightInput = findViewById(R.id.goldWeightInput);
        goldValueInput = findViewById(R.id.goldValueInput);
        goldTypeSpinner = findViewById(R.id.goldTypeSpinner);
        calculateButton = findViewById(R.id.calculateButton);

        // Set up spinner for gold types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gold_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goldTypeSpinner.setAdapter(adapter);

        goldTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                // Set uruf based on gold type
                if (selectedType.equals("Keep")) {
                    uruf = 85.0;
                } else if (selectedType.equals("Wear")) {
                    uruf = 200.0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                uruf = 0.0;
            }
        });

        // Add touch listener to hide the keyboard when Spinner is clicked
        goldTypeSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideKeyboard();
            }
            return false; // Allow default Spinner behavior
        });

        // Handle EditText focus to show the keyboard
        goldWeightInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showKeyboard(goldWeightInput);
        });

        goldValueInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showKeyboard(goldValueInput);
        });
        // Calculate button click listener
        calculateButton.setOnClickListener(v -> calculateZakat());
    }

    private void calculateZakat() {
        // Get inputs
        String weightStr = goldWeightInput.getText().toString();
        String valueStr = goldValueInput.getText().toString();

        if (weightStr.isEmpty() || valueStr.isEmpty()) {
            Toast.makeText(this, "Please enter valid inputs.", Toast.LENGTH_SHORT).show();
            return;
        }

        double weight = Double.parseDouble(weightStr);
        double valuePerGram = Double.parseDouble(valueStr);

        // Calculate total gold value
        double totalGoldValue = weight * valuePerGram;

        // Calculate zakat payable weight
        double zakatPayableWeight = weight - uruf;
        if (zakatPayableWeight < 0) {
            zakatPayableWeight = 0;
        }

        // Calculate zakat payable value
        double zakatPayableValue = zakatPayableWeight * valuePerGram;

        // Calculate total zakat
        double totalZakat = zakatPayableValue * 0.025;

        // Prepare result
        String result = String.format("Total Gold Value: RM%.2f\nZakat Payable Value: RM%.2f\nTotal Zakat: RM%.2f",
                totalGoldValue, zakatPayableValue, totalZakat);

        // Show result in dialog
        showResultDialog(result);
    }

    private void showResultDialog(String result) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Zakat Calculation Result");
        builder.setMessage(result);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override // Avoid closing the application on Back pressed
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Check the selected menu item using if-else
        int id = menuItem.getItemId();
        if (id == R.id.nav_about) {
            // Navigate to About (MainActivity)
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        } else if (id == R.id.nav_home) {
            // Navigate to Home (MainActivity)
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
            return true;
        } else if (id == R.id.nav_share) {
            // Share Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Use ZakatEase Now - https://t.co/app");
            startActivity(Intent.createChooser(shareIntent, null));
            return true;
        }else if (id == R.id.nav_copyrigth) {
            // Navigate to Copyright(MainActivity)
            Intent copyrightIntent = new Intent(this, CopyrightActivity.class);
            startActivity(copyrightIntent);
            return true;
        }

        // Close the drawer when an item is selected
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
