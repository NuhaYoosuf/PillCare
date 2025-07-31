package com.example.pillcare.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.pillcare

.models.Medication;
import com.example.pillcare

.models.MedicationLog;

@Database(entities = {Medication.class, MedicationLog.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class PillCareDatabase extends RoomDatabase {
    
    private static PillCareDatabase INSTANCE;
    
    public abstract MedicationDao medicationDao();
    public abstract MedicationLogDao medicationLogDao();
    
    public static synchronized PillCareDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PillCareDatabase.class, "pillcare_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}