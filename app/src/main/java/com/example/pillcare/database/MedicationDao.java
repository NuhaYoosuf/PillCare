package com.example.pillcare.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.pillcare.models.Medication;

import java.util.List;

@Dao
public interface MedicationDao {
    
    @Insert
    long insert(Medication medication);
    
    @Update
    void update(Medication medication);
    
    @Delete
    void delete(Medication medication);
    
    @Query("SELECT * FROM medications WHERE id = :id")
    LiveData<Medication> getMedicationById(int id);
    
    @Query("SELECT * FROM medications WHERE isActive = 1 ORDER BY name ASC")
    LiveData<List<Medication>> getAllActiveMedications();
    
    @Query("SELECT * FROM medications ORDER BY name ASC")
    LiveData<List<Medication>> getAllMedications();
    
    @Query("SELECT * FROM medications WHERE isActive = 1 AND reminderEnabled = 1")
    List<Medication> getMedicationsWithReminders();
    
    @Query("UPDATE medications SET isActive = 0 WHERE id = :medicationId")
    void deactivateMedication(int medicationId);
    
    @Query("UPDATE medications SET isActive = 1 WHERE id = :medicationId")
    void activateMedication(int medicationId);
    
    @Query("SELECT COUNT(*) FROM medications WHERE isActive = 1")
    LiveData<Integer> getActiveMedicationCount();
}