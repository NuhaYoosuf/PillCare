package com.example.pillcare.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.pillcare

.R;
import com.example.pillcare

.activities.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    
    private static final String CHANNEL_ID = "medication_reminders";
    private static final String CHANNEL_NAME = "Medication Reminders";
    private static final String CHANNEL_DESCRIPTION = "Notifications for medication reminders";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String medicationName = intent.getStringExtra("medication_name");
        String dosage = intent.getStringExtra("dosage");
        int medicationId = intent.getIntExtra("medication_id", -1);
        
        createNotificationChannel(context);
        showMedicationNotification(context, medicationName, dosage, medicationId);
    }
    
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    
    private void showMedicationNotification(Context context, String medicationName, String dosage, int medicationId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("medication_id", medicationId);
        
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                medicationId, 
                intent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // Action for marking as taken
        Intent takenIntent = new Intent(context, NotificationActionReceiver.class);
        takenIntent.setAction("ACTION_TAKEN");
        takenIntent.putExtra("medication_id", medicationId);
        PendingIntent takenPendingIntent = PendingIntent.getBroadcast(
                context, 
                medicationId * 10, 
                takenIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        // Action for snoozing
        Intent snoozeIntent = new Intent(context, NotificationActionReceiver.class);
        snoozeIntent.setAction("ACTION_SNOOZE");
        snoozeIntent.putExtra("medication_id", medicationId);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(
                context, 
                medicationId * 10 + 1, 
                snoozeIntent, 
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_pill_notification)
                .setContentTitle("Time for your medication!")
                .setContentText(medicationName + " - " + dosage)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 1000, 500, 1000})
                .addAction(R.drawable.ic_check, "Taken", takenPendingIntent)
                .addAction(R.drawable.ic_snooze, "Snooze", snoozePendingIntent);
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(medicationId, builder.build());
        }
    }
}