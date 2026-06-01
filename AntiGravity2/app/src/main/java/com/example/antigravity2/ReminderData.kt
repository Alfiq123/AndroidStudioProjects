package com.example.antigravity2

import org.json.JSONObject

data class ReminderData(
    val id: String,
    val subject: String,
    val timeInMillis: Long,
    val durationMinutes: Int
) {
    fun toJsonObject(): JSONObject {
        val json = JSONObject()
        json.put("id", id)
        json.put("subject", subject)
        json.put("timeInMillis", timeInMillis)
        json.put("durationMinutes", durationMinutes)
        return json
    }

    companion object {
        fun fromJsonObject(json: JSONObject): ReminderData {
            return ReminderData(
                id = json.getString("id"),
                subject = json.getString("subject"),
                timeInMillis = json.getLong("timeInMillis"),
                durationMinutes = json.getInt("durationMinutes")
            )
        }
    }
}
