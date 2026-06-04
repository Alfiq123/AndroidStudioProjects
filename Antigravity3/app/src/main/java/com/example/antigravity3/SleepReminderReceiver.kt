package com.example.antigravity3

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class SleepReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule alarm if user has it enabled
            val prefs = SleepPreferences(context)
            if (prefs.isReminderEnabled) {
                val manager = SleepReminderManager(context)
                manager.scheduleReminder(prefs.bedtimeHour, prefs.bedtimeMinute)
            }
            return
        }

        // Show Notification
        showNotification(context)
        
        // Reschedule for the next day
        val prefs = SleepPreferences(context)
        if (prefs.isReminderEnabled) {
            val manager = SleepReminderManager(context)
            manager.scheduleReminder(prefs.bedtimeHour, prefs.bedtimeMinute)
        }
    }

    private fun showNotification(context: Context) {
        val channelId = "sleep_reminder_channel"
        val notificationId = 1001

        // Create Channel if on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pengingat Tidur"
            val descriptionText = "Notifikasi untuk mengingatkan waktu tidur Anda"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to open MainActivity when tapped
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Waktunya Bersiap Tidur! 🌙")
            .setContentText("Ayo matikan gadget Anda dan bersiap untuk tidur malam yang nyenyak.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000))

        // Trigger notification
        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) 
                    == PackageManager.PERMISSION_GRANTED) {
                    try {
                        notify(notificationId, builder.build())
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                }
            } else {
                try {
                    notify(notificationId, builder.build())
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
