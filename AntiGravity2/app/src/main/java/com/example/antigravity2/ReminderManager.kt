package com.example.antigravity2

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class ReminderManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("study_reminders_prefs", Context.MODE_PRIVATE)

    fun getReminders(): List<ReminderData> {
        val list = mutableListOf<ReminderData>()
        val jsonString = prefs.getString("reminders", null) ?: return list
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                list.add(ReminderData.fromJsonObject(jsonObject))
            }
        } catch (e: Exception) {
            Log.e("ReminderManager", "Error parsing reminders: ${e.message}")
        }
        // Filter out expired reminders just in case (e.g. device was off and receiver didn't trigger yet)
        val now = System.currentTimeMillis()
        val validList = list.filter { it.timeInMillis > now }
        if (validList.size != list.size) {
            saveRemindersList(validList)
        }
        return validList
    }

    private fun saveRemindersList(list: List<ReminderData>) {
        val jsonArray = JSONArray()
        list.forEach {
            jsonArray.put(it.toJsonObject())
        }
        prefs.edit().putString("reminders", jsonArray.toString()).apply()
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleReminder(reminder: ReminderData): Boolean {
        // 1. Save to SharedPreferences
        val currentList = getReminders().toMutableList()
        currentList.add(reminder)
        saveRemindersList(currentList)

        // 2. Schedule using AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("id", reminder.id)
            putExtra("subject", reminder.subject)
            putExtra("duration", reminder.durationMinutes)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // For exact timing on modern devices
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            reminder.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        // Fallback to inexact but wake-up alarm if exact permission is not granted
                        alarmManager.setAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            reminder.timeInMillis,
                            pendingIntent
                        )
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminder.timeInMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    reminder.timeInMillis,
                    pendingIntent
                )
            }
            return true
        } catch (e: SecurityException) {
            Log.e("ReminderManager", "SecurityException scheduling exact alarm: ${e.message}")
            // Fallback to non-exact alarm
            try {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    reminder.timeInMillis,
                    pendingIntent
                )
                return true
            } catch (ex: Exception) {
                Log.e("ReminderManager", "Failed fallback alarm scheduling: ${ex.message}")
                return false
            }
        }
    }

    fun cancelReminder(reminderId: String) {
        // 1. Remove from SharedPreferences
        val currentList = getReminders().filter { it.id != reminderId }
        saveRemindersList(currentList)

        // 2. Cancel in AlarmManager
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun removeReminder(reminderId: String) {
        val currentList = getReminders().filter { it.id != reminderId }
        saveRemindersList(currentList)
    }
}
