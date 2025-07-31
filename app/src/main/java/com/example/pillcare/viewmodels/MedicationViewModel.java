package com.example.pillcare.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.pillcare

.database.PillCareDatabase;
import com.example.pillcare

.database.MedicationDao;
import com.example.pillcare

.models.Medication;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicationViewModel extends AndroidViewModel {
    
    private MedicationDao medicationDao;
    private LiveData<List<Medication>> allActiveMedications;
    private ExecutorService executor;
    
    public MedicationViewModel(@NonNull Application application) {
        super(application);
        PillCareDatabase database = PillCareDatabase.getDatabase(application);
        medicationDao = database.medicationDao();
        allActiveMedications = medicationDao.getAllActiveMedications();
        executor = Executors.newFixedThreadPool(4);
    }
    
    public LiveData<List<Medication>> getAllActiveMedications() {
        return allActiveMedications;
    }
    
    public LiveData<List<Medication>> getAllMedications() {
        return medicationDao.getAllMedications();
    }
    
    public LiveData<Medication> getMedicationById(int id) {
        return medicationDao.getMedicationById(id);
    }
    
    public LiveData<Integer> getActiveMedicationCount() {
        return medicationDao.getActiveMedicationCount();
    }
    
    public void insert(Medication medication) {
        executor.execute(() -> medicationDao.insert(medication));
    }
    
    public void update(Medication medication) {
        executor.execute(() -> medicationDao.update(medication));
    }
    
    public void delete(Medication medication) {
        executor.execute(() -> medicationDao.delete(medication));
    }
    
    public void activateMedication(int medicationId) {
        executor.execute(() -> medicationDao.activateMedication(medicationId));
    }
    
    public void deactivateMedication(int medicationId) {
        executor.execute(() -> medicationDao.deactivateMedication(medicationId));
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}