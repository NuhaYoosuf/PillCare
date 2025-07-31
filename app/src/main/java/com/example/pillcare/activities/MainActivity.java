package com.example.pillcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.example.pillcare.R;
import com.example.pillcare.adapters.MedicationAdapter;
import com.example.pillcare.models.Medication;
import com.example.pillcare.viewmodels.MedicationViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private RecyclerView medicationsRecyclerView;
    private MedicationAdapter medicationAdapter;
    private MedicationViewModel medicationViewModel;
    private View welcomeLayout;
    private View contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupNavigationDrawer();
        setupRecyclerView();
        setupViewModel();
        setupFab();
    }

    private void initViews() {
        drawer = findViewById(R.id.drawer_layout);
        medicationsRecyclerView = findViewById(R.id.medications_recycler_view);
        welcomeLayout = findViewById(R.id.welcome_layout);
        contentLayout = findViewById(R.id.content_layout);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, findViewById(R.id.toolbar), 
                R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupRecyclerView() {
        medicationAdapter = new MedicationAdapter(new ArrayList<>(), this::onMedicationClick, this::onTakeButtonClick);
        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationsRecyclerView.setAdapter(medicationAdapter);
    }

    private void setupViewModel() {
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        medicationViewModel.getAllActiveMedications().observe(this, medications -> {
            if (medications == null || medications.isEmpty()) {
                showWelcomeScreen();
            } else {
                showMedicationsList(medications);
            }
        });
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddMedicationActivity.class);
            startActivity(intent);
        });
    }

    private void showWelcomeScreen() {
        welcomeLayout.setVisibility(View.VISIBLE);
        contentLayout.setVisibility(View.GONE);
    }

    private void showMedicationsList(List<Medication> medications) {
        welcomeLayout.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
        medicationAdapter.updateMedications(medications);
    }

    private void onMedicationClick(Medication medication) {
        Intent intent = new Intent(this, MedicationDetailsActivity.class);
        intent.putExtra("medication_id", medication.getId());
        startActivity(intent);
    }

    private void onTakeButtonClick(Medication medication) {
        // Handle medication taken action
        // This would typically update the medication log
        // For now, we'll just show a toast
        android.widget.Toast.makeText(this, 
            medication.getName() + " marked as taken", 
            android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_settings) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_medications) {
            // Already on medications screen
        } else if (id == R.id.nav_today) {
            Intent intent = new Intent(this, DailyTrackerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_history) {
            // Navigate to history screen
        } else if (id == R.id.nav_settings) {
            // Navigate to settings screen
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}