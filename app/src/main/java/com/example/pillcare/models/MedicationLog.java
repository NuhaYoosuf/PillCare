package com.example.pillcare.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.pillcare

.database.Converters;

import java.util.Date;

@Entity(tableName = "medication_logs",
        foreignKeys = @ForeignKey(entity = Medication.class,
                                  parentColumns = "id",
                                  childColumns = "medicationId",
                                  onDelete = ForeignKey.CASCADE))
@TypeConverters(Converters.class)
public class MedicationLog {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int medicationId;
    private Date scheduledTime;
    private Date takenTime;
    private boolean wasTaken;
    private boolean wasMissed;
    private String notes;
    
    // Constructors
    public MedicationLog() {}
    
    public MedicationLog(int medicationId, Date scheduledTime) {
        this.medicationId = medicationId;
        this.scheduledTime = scheduledTime;
        this.wasTaken = false;
        this.wasMissed = false;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getMedicationId() { return medicationId; }
    public void setMedicationId(int medicationId) { this.medicationId = medicationId; }
    
    public Date getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(Date scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public Date getTakenTime() { return takenTime; }
    public void setTakenTime(Date takenTime) { this.takenTime = takenTime; }
    
    public boolean isWasTaken() { return wasTaken; }
    public void setWasTaken(boolean wasTaken) { this.wasTaken = wasTaken; }
    
    public boolean isWasMissed() { return wasMissed; }
    public void setWasMissed(boolean wasMissed) { this.wasMissed = wasMissed; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}