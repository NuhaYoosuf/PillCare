package com.example.pillcare.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pillcare

.R;
import com.example.pillcare

.adapters.MedicationAdapter;
import com.example.pillcare

.models.Medication;
import com.example.pillcare

.utils.DateTimeUtils;
import com.example.pillcare

.viewmodels.MedicationViewModel;

import java.util.ArrayList;
import java.util.Date;

public class DailyTrackerActivity extends AppCompatActivity {

    private TextView dateTitle, completionRate, medicationsTaken, medicationsMissed;
    private RecyclerView todayMedicationsRecyclerView;
    private MedicationAdapter medicationAdapter;
    private MedicationViewModel medicationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tracker);
        
        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        updateDateTitle();
    }

    private void initViews() {
        dateTitle = findViewById(R.id.date_title);
        completionRate = findViewById(R.id.completion_rate);
        medicationsTaken = findViewById(R.id.medications_taken);
        medicationsMissed = findViewById(R.id.medications_missed);
        todayMedicationsRecyclerView = findViewById(R.id.today_medications_recycler_view);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.daily_tracker);
        }
    }

    private void setupRecyclerView() {
        medicationAdapter = new MedicationAdapter(new ArrayList<>(), 
                this::onMedicationClick, this::onTakeButtonClick);
        todayMedicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todayMedicationsRecyclerView.setAdapter(medicationAdapter);
    }

    private void setupViewModel() {
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        medicationViewModel.getAllActiveMedications().observe(this, medications -> {
            if (medications != null) {
                medicationAdapter.updateMedications(medications);
                updateStatistics(medications);
            }
        });
    }

    private void updateDateTitle() {
        Date today = new Date();
        dateTitle.setText(DateTimeUtils.getRelativeDateString(today));
    }

    private void updateStatistics(java.util.List<Medication> medications) {
        // This is a simplified implementation
        // In a real app, you would calculate actual statistics from medication logs
        int totalMedications = medications.size();
        int taken = 0; // Would be calculated from logs
        int missed = 0; // Would be calculated from logs
        
        medicationsTaken.setText(String.valueOf(taken));
        medicationsMissed.setText(String.valueOf(missed));
        
        if (totalMedications > 0) {
            int rate = (taken * 100) / totalMedications;
            completionRate.setText(rate + "%");
        } else {
            completionRate.setText("0%");
        }
    }

    private void onMedicationClick(Medication medication) {
        // Handle medication click
    }

    private void onTakeButtonClick(Medication medication) {
        // Handle take button click
        android.widget.Toast.makeText(this, 
            medication.getName() + " marked as taken", 
            android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}