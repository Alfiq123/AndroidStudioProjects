package com.example.antigravity2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.antigravity2.ui.theme.AntiGravity2Theme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AntiGravity2Theme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F0E17) // Sleek obsidian/dark background
                ) {
                    StudyReminderScreen()
                }
            }
        }
    }
}

data class DurationOption(
    val label: String,
    val value: Int,
    val isSeconds: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyReminderScreen() {
    val context = LocalContext.current
    val reminderManager = remember { ReminderManager(context) }
    
    var subject by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableStateOf(15) }
    var isSecondsMode by remember { mutableStateOf(false) }
    
    val reminders = remember { mutableStateListOf<ReminderData>() }
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Load initial reminders
    LaunchedEffect(Unit) {
        reminders.addAll(reminderManager.getReminders())
        while (true) {
            currentTime = System.currentTimeMillis()
            
            // Auto refresh list from persistent storage to keep UI sync'd
            val currentReminders = reminderManager.getReminders()
            if (reminders.size != currentReminders.size || !reminders.map { it.id }.containsAll(currentReminders.map { it.id })) {
                reminders.clear()
                reminders.addAll(currentReminders)
            }
            delay(1000)
        }
    }

    val pendingReminderToSchedule = remember { mutableStateOf<ReminderData?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Izin notifikasi diberikan! 🔔", Toast.LENGTH_SHORT).show()
            pendingReminderToSchedule.value?.let { reminder ->
                val success = reminderManager.scheduleReminder(reminder)
                if (success) {
                    reminders.clear()
                    reminders.addAll(reminderManager.getReminders())
                    Toast.makeText(context, "Pengingat berhasil dijadwalkan! 🎉", Toast.LENGTH_SHORT).show()
                    subject = ""
                }
                pendingReminderToSchedule.value = null
            }
        } else {
            Toast.makeText(context, "Izin ditolak. Aplikasi tidak dapat memicu notifikasi.", Toast.LENGTH_LONG).show()
        }
    }

    fun scheduleStudy() {
        if (subject.trim().isEmpty()) {
            Toast.makeText(context, "Harap isi mata pelajaran! 📚", Toast.LENGTH_SHORT).show()
            return
        }

        val durationMillis = if (isSecondsMode) {
            5 * 1000L // 5 seconds for instant testing
        } else {
            durationMinutes * 60 * 1000L
        }

        val targetTime = System.currentTimeMillis() + durationMillis
        val newReminder = ReminderData(
            id = UUID.randomUUID().toString(),
            subject = subject.trim(),
            timeInMillis = targetTime,
            durationMinutes = if (isSecondsMode) 0 else durationMinutes
        )

        // Request POST_NOTIFICATIONS runtime permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                pendingReminderToSchedule.value = newReminder
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }

        // Schedule
        val success = reminderManager.scheduleReminder(newReminder)
        if (success) {
            reminders.clear()
            reminders.addAll(reminderManager.getReminders())
            Toast.makeText(context, "Pengingat belajar dijadwalkan! 🎉", Toast.LENGTH_SHORT).show()
            subject = ""
        } else {
            Toast.makeText(context, "Gagal menjadwalkan pengingat.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = Color(0xFF0F0E17),
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
        ) {
            // Header Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6246EA), Color(0xFFE45858))
                                )
                            )
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "EduRemind 📚",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Kelola dan jadwalkan sesi belajar belajarmu agar tetap fokus dan teratur.",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // Input Form Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF16162A)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Buat Pengingat Baru",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Mata Pelajaran / Topik", color = Color.White.copy(alpha = 0.6f)) },
                            placeholder = { Text("misal: Logika Kotlin, Fisika Dasar, Sejarah", color = Color.White.copy(alpha = 0.3f)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6246EA),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                                focusedLabelColor = Color(0xFF6246EA),
                                unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Durasi Belajar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        // Quick duration choices
                        val durationOptions = listOf(
                            DurationOption("5 Detik ⚡", 0, true),
                            DurationOption("15 Mnt", 15, false),
                            DurationOption("30 Mnt", 30, false),
                            DurationOption("60 Mnt", 60, false),
                            DurationOption("120 Mnt", 120, false)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            durationOptions.forEach { option ->
                                val isSelected = if (option.isSeconds) isSecondsMode else (!isSecondsMode && durationMinutes == option.value)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = if (isSelected) Color(0xFF6246EA) else Color.White.copy(alpha = 0.05f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            if (option.isSeconds) {
                                                isSecondsMode = true
                                            } else {
                                                isSecondsMode = false
                                                durationMinutes = option.value
                                            }
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = option.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Fine tuning slider (if not in seconds mode)
                        if (!isSecondsMode) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Sesuaikan Durasi Belajar:",
                                        fontSize = 13.sp,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$durationMinutes Menit",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE45858)
                                    )
                                }
                                Slider(
                                    value = durationMinutes.toFloat(),
                                    onValueChange = { durationMinutes = it.toInt() },
                                    valueRange = 1f..180f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = Color(0xFFE45858),
                                        activeTrackColor = Color(0xFF6246EA),
                                        inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            Text(
                                text = "💡 Mode Pengujian Instan: Notifikasi pengingat akan muncul tepat 5 detik setelah dijadwalkan.",
                                fontSize = 12.sp,
                                color = Color(0xFFFFD43F),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { scheduleStudy() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(Color(0xFF6246EA), Color(0xFFE45858))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Jadwalkan Belajar 🚀",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Reminders List Header
            item {
                Text(
                    text = "Daftar Pengingat Belajar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Reminders List content
            if (reminders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF16162A)),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp, horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⏳",
                                fontSize = 48.sp,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            Text(
                                text = "Belum Ada Jadwal Belajar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ayo, buat jadwal belajarmu sekarang agar waktu belajar lebih terkelola dan menyenangkan!",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.4f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(
                    items = reminders,
                    key = { it.id }
                ) { reminder ->
                    ReminderItemCard(
                        reminder = reminder,
                        currentTime = currentTime,
                        reminderManager = reminderManager,
                        onCancel = {
                            reminders.remove(reminder)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReminderItemCard(
    reminder: ReminderData,
    currentTime: Long,
    reminderManager: ReminderManager,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    
    val secondsLeft = remember(reminder.timeInMillis, currentTime) {
        ((reminder.timeInMillis - currentTime) / 1000).coerceAtLeast(0)
    }

    val countdownText = remember(secondsLeft) {
        if (secondsLeft <= 0) {
            "Memulai..."
        } else {
            val hrs = secondsLeft / 3600
            val mins = (secondsLeft % 3600) / 60
            val secs = secondsLeft % 60
            if (hrs > 0) {
                String.format("%02d jam %02d mnt %02d dtk", hrs, mins, secs)
            } else if (mins > 0) {
                String.format("%02d mnt %02d dtk", mins, secs)
            } else {
                String.format("%d dtk", secs)
            }
        }
    }

    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val targetTimeStr = remember(reminder.timeInMillis) { timeFormat.format(Date(reminder.timeInMillis)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16162A)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = reminder.subject,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (reminder.durationMinutes == 0) "Sesi Instan (5 dtk)" else "Durasi: ${reminder.durationMinutes} mnt",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = "Alarm: $targetTimeStr",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "⏱️ Sisa waktu:",
                        fontSize = 13.sp,
                        color = Color(0xFFE45858),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = countdownText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            IconButton(
                onClick = {
                    reminderManager.cancelReminder(reminder.id)
                    onCancel()
                    Toast.makeText(context, "Sesi belajar dibatalkan", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    text = "🗑️",
                    fontSize = 20.sp
                )
            }
        }
    }
}