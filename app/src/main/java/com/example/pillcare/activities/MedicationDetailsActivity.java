package com.example.pillcare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.example.pillcare

.R;
import com.example.pillcare

.models.Medication;
import com.example.pillcare

.utils.DateTimeUtils;
import com.example.pillcare

.utils.NotificationUtils;
import com.example.pillcare

.viewmodels.MedicationViewModel;

public class MedicationDetailsActivity extends AppCompatActivity {

    private TextView medicationName, medicationDosage, medicationDescription,
                     medicationFrequency, medicationTimes, medicationStartDate,
                     medicationEndDate, medicationInstructions;
    private SwitchMaterial switchActive, switchReminders;
    private MaterialButton btnEdit, btnDelete;
    
    private MedicationViewModel medicationViewModel;
    private Medication currentMedication;
    private int medicationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_details);
        
        medicationId = getIntent().getIntExtra("medication_id", -1);
        if (medicationId == -1) {
            finish();
            return;
        }
        
        initViews();
        setupToolbar();
        setupViewModel();
        setupClickListeners();
    }

    private void initViews() {
        medicationName = findViewById(R.id.medication_name);
        medicationDosage = findViewById(R.id.medication_dosage);
        medicationDescription = findViewById(R.id.medication_description);
        medicationFrequency = findViewById(R.id.medication_frequency);
        medicationTimes = findViewById(R.id.medication_times);
        medicationStartDate = findViewById(R.id.medication_start_date);
        medicationEndDate = findViewById(R.id.medication_end_date);
        medicationInstructions = findViewById(R.id.medication_instructions);
        
        switchActive = findViewById(R.id.switch_active);
        switchReminders = findViewById(R.id.switch_reminders);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViewModel() {
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        medicationViewModel.getMedicationById(medicationId).observe(this, medication -> {
            if (medication != null) {
                currentMedication = medication;
                populateViews(medication);
            }
        });
    }

    private void setupClickListeners() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditMedicationActivity.class);
            intent.putExtra("medication_id", medicationId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());

        switchActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentMedication != null) {
                if (isChecked) {
                    medicationViewModel.activateMedication(medicationId);
                } else {
                    medicationViewModel.deactivateMedication(medicationId);
                }
            }
        });

        switchReminders.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentMedication != null) {
                currentMedication.setReminderEnabled(isChecked);
                medicationViewModel.update(currentMedication);
                
                if (isChecked) {
                    NotificationUtils.scheduleMedicationReminders(this, currentMedication);
                    Toast.makeText(this, "Reminders enabled", Toast.LENGTH_SHORT).show();
                } else {
                    NotificationUtils.cancelMedicationReminders(this, currentMedication);
                    Toast.makeText(this, "Reminders disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateViews(Medication medication) {
        medicationName.setText(medication.getName());
        medicationDosage.setText(medication.getDosage());
        medicationDescription.setText(medication.getDescription());
        medicationFrequency.setText(medication.getFrequency() + " times per day");
        
        if (medication.getTimes() != null && !medication.getTimes().isEmpty()) {
            StringBuilder times = new StringBuilder();
            for (int i = 0; i < medication.getTimes().size(); i++) {
                times.append(medication.getTimes().get(i));
                if (i < medication.getTimes().size() - 1) {
                    times.append(", ");
                }
            }
            medicationTimes.setText(times.toString());
        } else {
            medicationTimes.setText("No times set");
        }
        
        medicationStartDate.setText(DateTimeUtils.formatDate(medication.getStartDate()));
        medicationEndDate.setText(DateTimeUtils.formatDate(medication.getEndDate()));
        medicationInstructions.setText(medication.getInstructions());
        
        switchActive.setChecked(medication.isActive());
        switchReminders.setChecked(medication.isReminderEnabled());
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(medication.getName());
        }
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medication")
                .setMessage("Are you sure you want to delete this medication? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (currentMedication != null) {
                        NotificationUtils.cancelMedicationReminders(this, currentMedication);
                        medicationViewModel.delete(currentMedication);
                        Toast.makeText(this, R.string.medication_deleted, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medication_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, EditMedicationActivity.class);
            intent.putExtra("medication_id", medicationId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}