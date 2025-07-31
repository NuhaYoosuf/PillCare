package com.example.pillcare.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.pillcare

.models.MedicationLog;

import java.util.Date;
import java.util.List;

@Dao
public interface MedicationLogDao {
    
    @Insert
    long insert(MedicationLog log);
    
    @Update
    void update(MedicationLog log);
    
    @Delete
    void delete(MedicationLog log);
    
    @Query("SELECT * FROM medication_logs WHERE id = :id")
    LiveData<MedicationLog> getLogById(int id);
    
    @Query("SELECT * FROM medication_logs WHERE medicationId = :medicationId ORDER BY scheduledTime DESC")
    LiveData<List<MedicationLog>> getLogsByMedicationId(int medicationId);
    
    @Query("SELECT * FROM medication_logs WHERE scheduledTime >= :startDate AND scheduledTime <= :endDate ORDER BY scheduledTime ASC")
    LiveData<List<MedicationLog>> getLogsByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT * FROM medication_logs WHERE wasTaken = 1 AND scheduledTime >= :startDate AND scheduledTime <= :endDate")
    LiveData<List<MedicationLog>> getTakenMedicationsByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT * FROM medication_logs WHERE wasMissed = 1 AND scheduledTime >= :startDate AND scheduledTime <= :endDate")
    LiveData<List<MedicationLog>> getMissedMedicationsByDateRange(Date startDate, Date endDate);
    
    @Query("SELECT COUNT(*) FROM medication_logs WHERE wasTaken = 1 AND scheduledTime >= :startDate AND scheduledTime <= :endDate")
    LiveData<Integer> getTakenMedicationCount(Date startDate, Date endDate);
    
    @Query("SELECT COUNT(*) FROM medication_logs WHERE wasMissed = 1 AND scheduledTime >= :startDate AND scheduledTime <= :endDate")
    LiveData<Integer> getMissedMedicationCount(Date startDate, Date endDate);
    
    @Query("UPDATE medication_logs SET wasTaken = 1, takenTime = :takenTime WHERE id = :logId")
    void markAsTaken(int logId, Date takenTime);
    
    @Query("UPDATE medication_logs SET wasMissed = 1 WHERE id = :logId")
    void markAsMissed(int logId);
}