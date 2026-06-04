package com.example.antigravity3

import android.content.Context
import android.content.SharedPreferences

class SleepPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("sleep_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BEDTIME_HOUR = "bedtime_hour"
        private const val KEY_BEDTIME_MINUTE = "bedtime_minute"
        private const val KEY_WAKE_HOUR = "wake_hour"
        private const val KEY_WAKE_MINUTE = "wake_minute"
        private const val KEY_REMINDER_ENABLED = "reminder_enabled"
    }

    var bedtimeHour: Int
        get() = prefs.getInt(KEY_BEDTIME_HOUR, 22)
        set(value) = prefs.edit().putInt(KEY_BEDTIME_HOUR, value).apply()

    var bedtimeMinute: Int
        get() = prefs.getInt(KEY_BEDTIME_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_BEDTIME_MINUTE, value).apply()

    var wakeHour: Int
        get() = prefs.getInt(KEY_WAKE_HOUR, 6)
        set(value) = prefs.edit().putInt(KEY_WAKE_HOUR, value).apply()

    var wakeMinute: Int
        get() = prefs.getInt(KEY_WAKE_MINUTE, 0)
        set(value) = prefs.edit().putInt(KEY_WAKE_MINUTE, value).apply()

    var isReminderEnabled: Boolean
        get() = prefs.getBoolean(KEY_REMINDER_ENABLED, false)
        set(value) = prefs.edit().putBoolean(KEY_REMINDER_ENABLED, value).apply()
}
