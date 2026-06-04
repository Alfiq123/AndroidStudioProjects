package com.example.antigravity3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepReminderScreen() {
    val context = LocalContext.current
    val prefs = remember { SleepPreferences(context) }
    val reminderManager = remember { SleepReminderManager(context) }

    // State for reminder activation
    var isReminderEnabled by remember { mutableStateOf(prefs.isReminderEnabled) }

    // State for bedtime
    var bedtimeHour by remember { mutableStateOf(prefs.bedtimeHour) }
    var bedtimeMinute by remember { mutableStateOf(prefs.bedtimeMinute) }

    // State for wake time
    var wakeHour by remember { mutableStateOf(prefs.wakeHour) }
    var wakeMinute by remember { mutableStateOf(prefs.wakeMinute) }

    // Dialog state
    var activeDialog by remember { mutableStateOf<TimePickerDialogType?>(null) }

    // Notification Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                isReminderEnabled = true
                prefs.isReminderEnabled = true
                reminderManager.scheduleReminder(bedtimeHour, bedtimeMinute)
            } else {
                isReminderEnabled = false
                prefs.isReminderEnabled = false
                reminderManager.cancelReminder()
            }
        }
    )

    // Calculate duration & feedback
    val (sleepHours, sleepMinutes) = calculateSleepDuration(
        bedtimeHour, bedtimeMinute, wakeHour, wakeMinute
    )
    val sleepQuality = evaluateSleepQuality(sleepHours)

    // Dynamic greeting based on device time
    val greeting = remember { getDynamicGreeting() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF070A1E),
                        Color(0xFF131735)
                    )
                )
            )
    ) {
        // Draw small glowing background stars
        Canvas(modifier = Modifier.fillMaxSize()) {
            val starPositions = listOf(
                Pair(0.1f, 0.15f), Pair(0.85f, 0.08f), Pair(0.3f, 0.4f),
                Pair(0.7f, 0.35f), Pair(0.9f, 0.55f), Pair(0.15f, 0.65f),
                Pair(0.8f, 0.8f), Pair(0.2f, 0.9f), Pair(0.5f, 0.75f)
            )
            starPositions.forEach { (x, y) ->
                drawCircle(
                    color = Color(0xFFFFDF6D).copy(alpha = 0.4f),
                    radius = 3.dp.toPx(),
                    center = center.copy(
                        x = size.width * x,
                        y = size.height * y
                    )
                )
                drawCircle(
                    color = Color.White,
                    radius = 1.5.dp.toPx(),
                    center = center.copy(
                        x = size.width * x,
                        y = size.height * y
                    )
                )
            }
        }

        // Main Content Scroll Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = greeting,
                        color = Color(0xFFB0B3C6),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Pengingat Tidur 🌙",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Small Moon Visual
                Canvas(modifier = Modifier.size(45.dp)) {
                    drawCircle(
                        color = Color(0xFFFFDF6D),
                        radius = size.minDimension / 2
                    )
                    drawCircle(
                        color = Color(0xFF070A1E),
                        radius = size.minDimension / 2,
                        center = center.copy(
                            x = center.x - size.minDimension * 0.22f,
                            y = center.y - size.minDimension * 0.08f
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Visual Sleep Dial (Ring Progress)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    val strokeWidth = 14.dp.toPx()
                    // Background Ring Track
                    drawCircle(
                        color = Color(0xFF1E2246),
                        style = Stroke(width = strokeWidth)
                    )

                    // Active Arc (representing sleep duration out of a 12 hour cycle)
                    val totalSleepMinutes = (sleepHours * 60 + sleepMinutes).toFloat()
                    val maxTargetMinutes = 12f * 60f // 12 hours is our circular max scale
                    val sweepAngle = (totalSleepMinutes / maxTargetMinutes * 360f).coerceAtMost(360f)

                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF8A93FC),
                                Color(0xFFFFDF6D),
                                Color(0xFF8A93FC)
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }

                // Inner content of the ring
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Durasi Tidur",
                        color = Color(0xFFB0B3C6),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (sleepMinutes > 0) "${sleepHours}j ${sleepMinutes}m" else "${sleepHours} jam",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(sleepQuality.badgeColor.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = sleepQuality.badgeText,
                            color = sleepQuality.badgeColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sleep Duration Health Feedback Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131735))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = sleepQuality.infoIcon,
                        contentDescription = "Sleep Feedback Icon",
                        tint = sleepQuality.badgeColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = sleepQuality.description,
                        color = Color(0xFFB0B3C6),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Time Selector Cards (Bedtime & Wake time side-by-side)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Bedtime Card
                TimePickerCard(
                    modifier = Modifier.weight(1f),
                    title = "Jam Tidur 🌌",
                    hour = bedtimeHour,
                    minute = bedtimeMinute,
                    onClick = { activeDialog = TimePickerDialogType.BEDTIME }
                )

                // Wake time Card
                TimePickerCard(
                    modifier = Modifier.weight(1f),
                    title = "Jam Bangun 🌅",
                    hour = wakeHour,
                    minute = wakeMinute,
                    onClick = { activeDialog = TimePickerDialogType.WAKETIME }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Reminder Toggle Switch Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131735))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isReminderEnabled) Color(0xFF8A93FC).copy(alpha = 0.2f)
                                    else Color(0xFF1E2246)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isReminderEnabled) Icons.Default.Notifications else Icons.Default.Notifications,
                                contentDescription = "Alarm Icon",
                                tint = if (isReminderEnabled) Color(0xFF8A93FC) else Color(0xFFB0B3C6)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Pengingat Tidur Harian",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (isReminderEnabled) "Aktif pada ${formatTime(bedtimeHour, bedtimeMinute)}" else "Nonaktif",
                                color = Color(0xFFB0B3C6),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Switch(
                        checked = isReminderEnabled,
                        onCheckedChange = { checked ->
                            if (checked) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    val permissionStatus = ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    )
                                    if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                                        isReminderEnabled = true
                                        prefs.isReminderEnabled = true
                                        reminderManager.scheduleReminder(bedtimeHour, bedtimeMinute)
                                    } else {
                                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                } else {
                                    isReminderEnabled = true
                                    prefs.isReminderEnabled = true
                                    reminderManager.scheduleReminder(bedtimeHour, bedtimeMinute)
                                }
                            } else {
                                isReminderEnabled = false
                                prefs.isReminderEnabled = false
                                reminderManager.cancelReminder()
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF8A93FC),
                            uncheckedThumbColor = Color(0xFFB0B3C6),
                            uncheckedTrackColor = Color(0xFF1E2246)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sleep Healthy Tips Section
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Tips Tidur Nyenyak 💡",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(getSleepTips()) { tip ->
                        SleepTipCard(tip)
                    }
                }
            }
        }
    }

    // Custom Time Picker Dialog
    if (activeDialog != null) {
        val currentInitialHour = if (activeDialog == TimePickerDialogType.BEDTIME) bedtimeHour else wakeHour
        val currentInitialMinute = if (activeDialog == TimePickerDialogType.BEDTIME) bedtimeMinute else wakeMinute

        val state = rememberTimePickerState(
            initialHour = currentInitialHour,
            initialMinute = currentInitialMinute,
            is24Hour = true
        )

        Dialog(
            onDismissRequest = { activeDialog = null }
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131735)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (activeDialog == TimePickerDialogType.BEDTIME) "Pilih Waktu Tidur" else "Pilih Waktu Bangun",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Customize colors of material3 TimePicker to match starry night
                    val timePickerColors = TimePickerDefaults.colors(
                        clockDialColor = Color(0xFF070A1E),
                        clockDialSelectedContentColor = Color.White,
                        clockDialUnselectedContentColor = Color(0xFFB0B3C6),
                        selectorColor = Color(0xFF8A93FC),
                        periodSelectorBorderColor = Color(0xFF1E2246),
                        periodSelectorSelectedContainerColor = Color(0xFF8A93FC),
                        periodSelectorUnselectedContainerColor = Color(0xFF070A1E),
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContentColor = Color(0xFFB0B3C6),
                        timeSelectorSelectedContainerColor = Color(0xFF8A93FC),
                        timeSelectorUnselectedContainerColor = Color(0xFF070A1E),
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContentColor = Color(0xFFB0B3C6)
                    )

                    TimePicker(
                        state = state,
                        colors = timePickerColors
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { activeDialog = null }
                        ) {
                            Text("Batal", color = Color(0xFFB0B3C6))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                if (activeDialog == TimePickerDialogType.BEDTIME) {
                                    bedtimeHour = state.hour
                                    bedtimeMinute = state.minute
                                    prefs.bedtimeHour = state.hour
                                    prefs.bedtimeMinute = state.minute
                                    // If active, reschedule alarm
                                    if (isReminderEnabled) {
                                        reminderManager.scheduleReminder(state.hour, state.minute)
                                    }
                                } else {
                                    wakeHour = state.hour
                                    wakeMinute = state.minute
                                    prefs.wakeHour = state.hour
                                    prefs.wakeMinute = state.minute
                                }
                                activeDialog = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF8A93FC),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Simpan")
                        }
                    }
                }
            }
        }
    }
}

// Side-by-side Time Pick Selector Card
@Composable
fun TimePickerCard(
    modifier: Modifier = Modifier,
    title: String,
    hour: Int,
    minute: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131735))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = Color(0xFFB0B3C6),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatTime(hour, minute),
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Ubah",
                color = Color(0xFF8A93FC),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Sleep Tip card rendered in Horizontal LazyRow
@Composable
fun SleepTipCard(tip: SleepTip) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131735))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = tip.icon,
                    contentDescription = null,
                    tint = Color(0xFFFFDF6D),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = tip.title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = tip.desc,
                color = Color(0xFFB0B3C6),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                maxLines = 4
            )
        }
    }
}

// Helper Enums & Data Classes
enum class TimePickerDialogType {
    BEDTIME, WAKETIME
}

data class SleepTip(
    val title: String,
    val desc: String,
    val icon: ImageVector
)

data class SleepFeedback(
    val badgeText: String,
    val badgeColor: Color,
    val infoIcon: ImageVector,
    val description: String
)

// Calculation Utilities
fun calculateSleepDuration(bedHour: Int, bedMin: Int, wakeHour: Int, wakeMin: Int): Pair<Int, Int> {
    val bedMinutes = bedHour * 60 + bedMin
    val wakeMinutes = wakeHour * 60 + wakeMin
    val diff = if (wakeMinutes > bedMinutes) {
        wakeMinutes - bedMinutes
    } else {
        (24 * 60 - bedMinutes) + wakeMinutes
    }
    return Pair(diff / 60, diff % 60)
}

fun evaluateSleepQuality(hours: Int): SleepFeedback {
    return when {
        hours < 6 -> SleepFeedback(
            badgeText = "Kurang Tidur",
            badgeColor = Color(0xFFEF5350),
            infoIcon = Icons.Default.Warning,
            description = "Durasi tidur Anda berada di bawah batas sehat. Kurang tidur berkepanjangan dapat memicu stres, kelelahan, dan penurunan imun."
        )
        hours in 6..7 -> SleepFeedback(
            badgeText = "Tidur Sedang",
            badgeColor = Color(0xFFFFB74D),
            infoIcon = Icons.Default.Info,
            description = "Tidur Anda cukup, namun masih sedikit di bawah batas optimal 8 jam. Cobalah tidur 30 menit lebih awal malam ini."
        )
        hours in 8..9 -> SleepFeedback(
            badgeText = "Tidur Ideal",
            badgeColor = Color(0xFF66BB6A),
            infoIcon = Icons.Default.CheckCircle,
            description = "Durasi tidur yang sangat sempurna! Tubuh Anda akan memiliki waktu yang optimal untuk pemulihan dan pembersihan kognitif."
        )
        else -> SleepFeedback(
            badgeText = "Tidur Panjang",
            badgeColor = Color(0xFF29B6F6),
            infoIcon = Icons.Default.Star,
            description = "Anda tidur di atas rata-rata normal. Ini baik jika Anda sedang memulihkan diri dari aktivitas berat atau mengganti hutang tidur."
        )
    }
}

fun formatTime(hour: Int, minute: Int): String {
    return String.format("%02d:%02d", hour, minute)
}

fun getDynamicGreeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> "Selamat Pagi! 🌅"
        in 12..17 -> "Selamat Siang! ☀️"
        else -> "Selamat Malam! 🌙"
    }
}

fun getSleepTips(): List<SleepTip> {
    return listOf(
        SleepTip(
            title = "Matikan Layar",
            desc = "Sinar biru HP menghambat hormon melatonin. Letakkan gadget 1 jam sebelum tidur.",
            icon = Icons.Default.Close
        ),
        SleepTip(
            title = "Suhu Kamar sejuk",
            desc = "Atur suhu kamar Anda agar tetap sejuk (20-22°C) untuk merangsang ngantuk alami.",
            icon = Icons.Default.Home
        ),
        SleepTip(
            title = "Batasi Kafein",
            desc = "Hindari kopi, teh pekat, atau minuman bersoda minimal 6 jam sebelum jadwal tidur.",
            icon = Icons.Default.Favorite
        ),
        SleepTip(
            title = "Jadwal Konsisten",
            desc = "Tidur dan bangun pada jam yang sama setiap hari agar sirkadian tubuh terlatih.",
            icon = Icons.Default.Notifications
        )
    )
}
