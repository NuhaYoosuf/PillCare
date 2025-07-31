package com.example.pillcare.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.example.pillcare

.models.Medication;
import com.example.pillcare

.services.AlarmReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationUtils {
    
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    
    public static void scheduleMedicationReminders(Context context, Medication medication) {
        if (!medication.isReminderEnabled() || !medication.isActive()) {
            return;
        }
        
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;
        
        List<String> times = medication.getTimes();
        if (times == null || times.isEmpty()) return;
        
        for (int i = 0; i < times.size(); i++) {
            String timeString = times.get(i);
            try {
                Date time = TIME_FORMAT.parse(timeString);
                if (time != null) {
                    Calendar calendar = Calendar.getInstance();
                    Calendar timeCalendar = Calendar.getInstance();
                    timeCalendar.setTime(time);
                    
                    calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                    calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    
                    // If the time has already passed today, schedule for tomorrow
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    
                    // Subtract reminder minutes
                    calendar.add(Calendar.MINUTE, -medication.getReminderMinutesBefore());
                    
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    intent.putExtra("medication_name", medication.getName());
                    intent.putExtra("dosage", medication.getDosage());
                    intent.putExtra("medication_id", medication.getId());
                    
                    int requestCode = medication.getId() * 100 + i;
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            context,
                            requestCode,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    } else {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.getTimeInMillis(),
                                pendingIntent
                        );
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void cancelMedicationReminders(Context context, Medication medication) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;
        
        List<String> times = medication.getTimes();
        if (times == null) return;
        
        for (int i = 0; i < times.size(); i++) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            int requestCode = medication.getId() * 100 + i;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            
            alarmManager.cancel(pendingIntent);
        }
    }
    
    public static void scheduleSnoozeReminder(Context context, int medicationId, int snoozeMinutes) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, snoozeMinutes);
        
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("medication_id", medicationId);
        
        int requestCode = medicationId * 1000; // Different request code for snooze
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        } else {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }
}