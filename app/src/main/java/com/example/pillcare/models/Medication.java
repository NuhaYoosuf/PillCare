package com.example.pillcare.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.pillcare

.database.Converters;

import java.util.Date;
import java.util.List;

@Entity(tableName = "medications")
@TypeConverters(Converters.class)
public class Medication {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private String description;
    private String dosage;
    private int frequency; // times per day
    private List<String> times; // specific times to take medication
    private Date startDate;
    private Date endDate;
    private boolean isActive;
    private String instructions;
    private String color; // for UI customization
    private boolean reminderEnabled;
    private int reminderMinutesBefore;
    
    // Constructors
    public Medication() {}
    
    public Medication(String name, String description, String dosage, int frequency, 
                     List<String> times, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.frequency = frequency;
        this.times = times;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = true;
        this.reminderEnabled = true;
        this.reminderMinutesBefore = 0;
        this.color = "#2196F3"; // Default blue color
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }
    
    public List<String> getTimes() { return times; }
    public void setTimes(List<String> times) { this.times = times; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public boolean isReminderEnabled() { return reminderEnabled; }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }
    
    public int getReminderMinutesBefore() { return reminderMinutesBefore; }
    public void setReminderMinutesBefore(int reminderMinutesBefore) { 
        this.reminderMinutesBefore = reminderMinutesBefore; 
    }
}