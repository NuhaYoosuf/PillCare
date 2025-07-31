package com.example.pillcare.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMedicationActivity extends AppCompatActivity {

    TextInputEditText editMedicationName;
    TextInputEditText editDescription;
    TextInputEditText editDosage;
    TextInputEditText editFrequency;
    TextInputEditText editInstructions;
    TextInputEditText editReminderMinutes;
    ChipGroup chipGroupTimes;
    private MaterialButton btnAddTime;
    MaterialButton btnStartDate;
    MaterialButton btnEndDate;
    private MaterialButton btnSave;
    private MaterialButton btnCancel;
    SwitchMaterial switchReminders;
    
    MedicationViewModel medicationViewModel;
    Date startDate;
    Date endDate;
    List<String> selectedTimes;
    public AddMedicationActivity() {
        // Required empty constructor
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);
        
        initViews();
        setupViewModel();
        setupClickListeners();
        initializeDefaults();
    }

    private void initViews() {
        editMedicationName = findViewById(R.id.edit_medication_name);
        editDescription = findViewById(R.id.edit_description);
        editDosage = findViewById(R.id.edit_dosage);
        editFrequency = findViewById(R.id.edit_frequency);
        editInstructions = findViewById(R.id.edit_instructions);
        editReminderMinutes = findViewById(R.id.edit_reminder_minutes);
        
        chipGroupTimes = findViewById(R.id.chip_group_times);
        btnAddTime = findViewById(R.id.btn_add_time);
        btnStartDate = findViewById(R.id.btn_start_date);
        btnEndDate = findViewById(R.id.btn_end_date);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        switchReminders = findViewById(R.id.switch_reminders);
        
        selectedTimes = new ArrayList<>();
    }

    private void setupViewModel() {
        medicationViewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
    }

    private void setupClickListeners() {
        btnAddTime.setOnClickListener(v -> showTimePickerDialog());
        btnStartDate.setOnClickListener(v -> showDatePickerDialog(true));
        btnEndDate.setOnClickListener(v -> showDatePickerDialog(false));
        btnSave.setOnClickListener(v -> saveMedication());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void initializeDefaults() {
        startDate = new Date();
        btnStartDate.setText(DateTimeUtils.formatDate(startDate));
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        endDate = calendar.getTime();
        btnEndDate.setText(DateTimeUtils.formatDate(endDate));
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minuteOfHour) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    if (!selectedTimes.contains(time)) {
                        selectedTimes.add(time);
                        addTimeChip(time);
                    }
                }, hour, minute, true);
        
        timePickerDialog.show();
    }

    void addTimeChip(String time) {
        Chip chip = new Chip(this);
        chip.setText(time);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            chipGroupTimes.removeView(chip);
            selectedTimes.remove(time);
        });
        chipGroupTimes.addView(chip);
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    Date selectedDate = calendar.getTime();
                    
                    if (isStartDate) {
                        startDate = selectedDate;
                        btnStartDate.setText(DateTimeUtils.formatDate(startDate));
                    } else {
                        endDate = selectedDate;
                        btnEndDate.setText(DateTimeUtils.formatDate(endDate));
                    }
                }, calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH));
        
        datePickerDialog.show();
    }

    private void saveMedication() {
        if (!validateInput()) {
            return;
        }

        String name = editMedicationName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String dosage = editDosage.getText().toString().trim();
        int frequency = Integer.parseInt(editFrequency.getText().toString());
        String instructions = editInstructions.getText().toString().trim();
        boolean reminderEnabled = switchReminders.isChecked();
        int reminderMinutes = Integer.parseInt(editReminderMinutes.getText().toString());

        Medication medication = new Medication(name, description, dosage, frequency, 
                                             selectedTimes, startDate, endDate);
        medication.setInstructions(instructions);
        medication.setReminderEnabled(reminderEnabled);
        medication.setReminderMinutesBefore(reminderMinutes);

        medicationViewModel.insert(medication);
        
        // Schedule notifications if enabled
        if (reminderEnabled) {
            NotificationUtils.scheduleMedicationReminders(this, medication);
        }

        Toast.makeText(this, R.string.medication_added, Toast.LENGTH_SHORT).show();
        finish();
    }

    boolean validateInput() {
        if (TextUtils.isEmpty(editMedicationName.getText())) {
            editMedicationName.setError(getString(R.string.error_empty_name));
            return false;
        }

        if (TextUtils.isEmpty(editDosage.getText())) {
            editDosage.setError(getString(R.string.error_empty_dosage));
            return false;
        }

        if (selectedTimes.isEmpty()) {
            Toast.makeText(this, R.string.error_no_times, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (endDate.before(startDate)) {
            Toast.makeText(this, R.string.error_invalid_date, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}