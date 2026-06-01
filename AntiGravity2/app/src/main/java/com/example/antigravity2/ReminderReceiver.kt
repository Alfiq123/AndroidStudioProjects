package com.example.antigravity2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val subject = intent.getStringExtra("subject") ?: "Belajar"
        val duration = intent.getIntExtra("duration", 0)
        val id = intent.getStringExtra("id") ?: ""

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "study_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Study Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pengingat waktu belajar"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to launch MainActivity when clicking the notification
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            id.hashCode(),
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val textContent = if (duration > 0) {
            "Saatnya belajar mata pelajaran: $subject selama $duration menit!"
        } else {
            "Saatnya belajar mata pelajaran: $subject!"
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Safe, built-in system icon
            .setContentTitle("Waktunya Belajar! 📚")
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(id.hashCode(), notification)

        // Clean up from persistent storage
        if (id.isNotEmpty()) {
            ReminderManager(context).removeReminder(id)
        }
    }
}
