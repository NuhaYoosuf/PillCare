package com.example.pillcare.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.example.pillcare.R;
import com.example.pillcare.models.Medication;
import com.example.pillcare.utils.DateTimeUtils;
import com.example.pillcare.utils.NotificationUtils;

public class EditMedicationActivity extends AddMedicationActivity {

    private int medicationId;
    private Medication currentMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // medicationViewModel is inherited from AddMedicationActivity (must be protected there)

        medicationId = getIntent().getIntExtra("medication_id", -1);
        if (medicationId == -1) {
            finish();
            return;
        }

        setupEditMode();
    }

    private void setupEditMode() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.edit_medication);
        }

        // Observe and populate UI
        medicationViewModel.getMedicationById(medicationId).observe(this, medication -> {
            if (medication != null) {
                currentMedication = medication;
                populateFields(medication);
            }
        });
    }

    private void populateFields(Medication medication) {
        editMedicationName.setText(medication.getName());
        editDescription.setText(medication.getDescription());
        editDosage.setText(medication.getDosage());
        editFrequency.setText(String.valueOf(medication.getFrequency()));
        editInstructions.setText(medication.getInstructions());
        editReminderMinutes.setText(String.valueOf(medication.getReminderMinutesBefore()));

        switchReminders.setChecked(medication.isReminderEnabled());

        if (medication.getStartDate() != null) {
            startDate = medication.getStartDate();
            btnStartDate.setText(DateTimeUtils.formatDate(startDate));
        }

        if (medication.getEndDate() != null) {
            endDate = medication.getEndDate();
            btnEndDate.setText(DateTimeUtils.formatDate(endDate));
        }

        if (medication.getTimes() != null) {
            selectedTimes.clear();
            selectedTimes.addAll(medication.getTimes());

            chipGroupTimes.removeAllViews();
            for (String time : selectedTimes) {
                addTimeChip(time);
            }
        }
    }


    protected void saveMedication() {
        if (!validateInput()) {
            return;
        }

        if (currentMedication != null) {
            NotificationUtils.cancelMedicationReminders(this, currentMedication);
        }

        String name = editMedicationName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String dosage = editDosage.getText().toString().trim();
        int frequency = Integer.parseInt(editFrequency.getText().toString());
        String instructions = editInstructions.getText().toString().trim();
        boolean reminderEnabled = switchReminders.isChecked();
        int reminderMinutes = Integer.parseInt(editReminderMinutes.getText().toString());

        currentMedication.setName(name);
        currentMedication.setDescription(description);
        currentMedication.setDosage(dosage);
        currentMedication.setFrequency(frequency);
        currentMedication.setTimes(selectedTimes);
        currentMedication.setStartDate(startDate);
        currentMedication.setEndDate(endDate);
        currentMedication.setInstructions(instructions);
        currentMedication.setReminderEnabled(reminderEnabled);
        currentMedication.setReminderMinutesBefore(reminderMinutes);

        medicationViewModel.update(currentMedication);

        if (reminderEnabled) {
            NotificationUtils.scheduleMedicationReminders(this, currentMedication);
        }

        Toast.makeText(this, R.string.medication_updated, Toast.LENGTH_SHORT).show();
        finish();
    }
}
