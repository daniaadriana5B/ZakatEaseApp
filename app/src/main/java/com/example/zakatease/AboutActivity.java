package com.example.zakatease;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about); //ensure the page is about when "ABOUT is click

        //Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.about_toolbar);

        //Tool Bar
        setSupportActionBar(toolbar);

        //Navigation Drawer Menu
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_about);
    }

    @Override //to avoid closing the application on Back pressed
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        // Check the selected menu item using if-else
        int id = menuItem.getItemId();
        if (id == R.id.nav_about) {
            // Stay in AboutActivity
            Toast.makeText(this, "You are already on the About page", Toast.LENGTH_SHORT).show();
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
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Use ZakatEase Now - https://github.com/daniaadriana5B/ZakatEaseApp");
            startActivity(Intent.createChooser(shareIntent, null));
            return true;
        }else if (id == R.id.nav_copyrigth) {
            // Navigate to Copyright (MainActivity)
            Intent copyrightIntent = new Intent(this, CopyrightActivity.class);
            startActivity(copyrightIntent );
            return true;
        }
        // Close the drawer when an item is selected
        drawerLayout.closeDrawer(GravityCompat.START);
        return false; // Default case
    }
}