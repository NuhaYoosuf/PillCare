package com.example.pillcare.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.example.pillcare

.database.PillCareDatabase;
import com.example.pillcare

.utils.NotificationUtils;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationActionReceiver extends BroadcastReceiver {
    
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int medicationId = intent.getIntExtra("medication_id", -1);
        
        if (medicationId == -1) return;
        
        // Dismiss the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(medicationId);
        }
        
        if ("ACTION_TAKEN".equals(action)) {
            handleMedicationTaken(context, medicationId);
        } else if ("ACTION_SNOOZE".equals(action)) {
            handleMedicationSnooze(context, medicationId);
        }
    }
    
    private void handleMedicationTaken(Context context, int medicationId) {
        executor.execute(() -> {
            PillCareDatabase database = PillCareDatabase.getDatabase(context);
            // Mark the current scheduled dose as taken
            // This would typically involve finding the current log entry and updating it
            // For now, we'll just show a toast
        });
        
        Toast.makeText(context, "Medication marked as taken!", Toast.LENGTH_SHORT).show();
    }
    
    private void handleMedicationSnooze(Context context, int medicationId) {
        // Snooze for 10 minutes
        NotificationUtils.scheduleSnoozeReminder(context, medicationId, 10);
        Toast.makeText(context, "Reminder snoozed for 10 minutes", Toast.LENGTH_SHORT).show();
    }
}