package com.example.antigravity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antigravity.ui.theme.AntiGravityTheme
import kotlinx.coroutines.delay
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AntiGravityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AntiGravityConsole(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AntiGravityConsole(modifier: Modifier = Modifier) {
    // States
    var gravityMode by remember { mutableStateOf(2) } // 0 = OFFLINE, 1 = LOW G, 2 = OPTIMAL G, 3 = HYPER G
    var thrustersActive by remember { mutableStateOf(false) }
    var shieldCharge by remember { mutableStateOf(0.85f) }
    var warpFactor by remember { mutableStateOf(1) } // 0 = STABILIZED, 1 = WARP 1x, 2 = WARP 3.5x, 3 = WARP 9.9x
    var shieldRecharging by remember { mutableStateOf(false) }

    // Dynamic stats derived from states
    val baseTemp = 24.5f
    val reactorTemp by animateFloatAsState(
        targetValue = baseTemp + 
                (if (thrustersActive) 48.2f else 0f) + 
                (gravityMode * 8.5f) + 
                (warpFactor * 12.1f) +
                (if (shieldRecharging) 18.4f else 0f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "reactorTemp"
    )

    val reactorStability by animateFloatAsState(
        targetValue = 100f - 
                (if (thrustersActive) 8.5f else 0f) - 
                (if (gravityMode == 3) 12.4f else 0f) - 
                (warpFactor * 4.2f) + 
                (if (gravityMode == 0) 3.5f else 0f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "reactorStability"
    )

    val energyOutput by animateFloatAsState(
        targetValue = 12.4f + 
                (if (thrustersActive) 45.0f else 0f) + 
                (gravityMode * 15.2f) + 
                (warpFactor * 22.8f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "energyOutput"
    )

    // Shield charging routine
    LaunchedEffect(shieldRecharging) {
        if (shieldRecharging) {
            val startCharge = shieldCharge
            val duration = ((1f - startCharge) * 3000).toLong().coerceAtLeast(500)
            val startTime = System.currentTimeMillis()
            while (shieldCharge < 1f) {
                val elapsed = System.currentTimeMillis() - startTime
                val progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
                shieldCharge = startCharge + (1f - startCharge) * progress
                delay(30)
            }
            shieldCharge = 1.0f
            shieldRecharging = false
        }
    }

    // Infinite transitions for smooth sci-fi animations
    val infiniteTransition = rememberInfiniteTransition(label = "StarfieldAndIcons")
    val twinklePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinkle"
    )

    val gravityPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gravity"
    )

    val flamePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flame"
    )

    val shieldPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shieldPulse"
    )

    val temporalPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000 / warpFactor.coerceAtLeast(1), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "temporal"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0C091A),
                        Color(0xFF040307)
                    )
                )
            )
    ) {
        // Starfield Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val starCount = 35
            for (i in 0 until starCount) {
                val x = (i * 149 + 47) % size.width
                val y = (i * 263 + 89) % size.height
                val alpha = 0.15f + 0.85f * sin(twinklePhase + i).coerceIn(0f, 1f)
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = (i % 3 + 1.2f).toFloat(),
                    center = Offset(x, y)
                )
            }
        }

        // Main Control Console Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ANTIGRAVITY",
                    color = Color(0xFF00E5FF),
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp,
                    modifier = Modifier.drawBehind {
                        drawLine(
                            color = Color(0xFF00E5FF).copy(alpha = 0.3f),
                            start = Offset(-20.dp.toPx(), size.height + 6.dp.toPx()),
                            end = Offset(size.width + 20.dp.toPx(), size.height + 6.dp.toPx()),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color(0xFF00E676), RoundedCornerShape(50))
                            .drawBehind {
                                drawCircle(
                                    color = Color(0xFF00E676).copy(alpha = 0.5f),
                                    radius = size.minDimension * 2f
                                )
                            }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SYSTEM CONTROLLER // ONLINE",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 2.sp
                    )
                }
            }

            // Real-time Status Panel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF141324).copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Temperature Stat
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = "REACTOR TEMP", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.1f°C", reactorTemp),
                            color = if (reactorTemp > 70f) Color(0xFFFF5252) else Color(0xFFFF9100),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color.White.copy(alpha = 0.1f)))

                    // Stability Stat
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = "STABILITY", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.1f%%", reactorStability),
                            color = if (reactorStability < 85f) Color(0xFFFF5252) else Color(0xFF00E676),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Divider
                    Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color.White.copy(alpha = 0.1f)))

                    // Energy Output Stat
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(text = "OUTPUT", color = Color.White.copy(alpha = 0.5f), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.1f GW", energyOutput),
                            color = Color(0xFF00E5FF),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // 2x2 Interactive Box Grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Box 1: Gravity Field
                    val gravityStatus = when (gravityMode) {
                        0 -> "OFFLINE"
                        1 -> "LOW G"
                        2 -> "OPTIMAL G"
                        else -> "HYPER G"
                    }
                    val gravityColor = when (gravityMode) {
                        0 -> Color(0xFFFF5252)
                        1 -> Color(0xFF00E5FF).copy(alpha = 0.6f)
                        2 -> Color(0xFF00E5FF)
                        else -> Color(0xFFFF1744)
                    }
                    SciFiCard(
                        title = "Gravity Field",
                        statusText = gravityStatus,
                        statusColor = gravityColor,
                        accentColor = Color(0xFF00E5FF),
                        onClick = {
                            gravityMode = (gravityMode + 1) % 4
                        },
                        modifier = Modifier.weight(1f)
                    ) { color ->
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            rotate(degrees = if (gravityMode > 0) gravityPhase * (gravityMode * 0.7f) else 0f) {
                                val strokeWidth = 2.dp.toPx()
                                drawCircle(
                                    color = color.copy(alpha = 0.3f),
                                    radius = 16.dp.toPx(),
                                    style = Stroke(
                                        width = strokeWidth,
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                                    )
                                )
                                drawCircle(
                                    color = color,
                                    radius = 9.dp.toPx(),
                                    style = Stroke(width = strokeWidth)
                                )
                                drawCircle(
                                    color = color,
                                    radius = 3.dp.toPx(),
                                    style = Stroke(width = strokeWidth)
                                )
                            }
                        }
                    }

                    // Box 2: Quantum Thrusters
                    SciFiCard(
                        title = "Thrusters",
                        statusText = if (thrustersActive) "FIRING (MAX)" else "READY",
                        statusColor = if (thrustersActive) Color(0xFFFF4081) else Color(0xFF00E676),
                        accentColor = Color(0xFFE040FB),
                        onClick = {
                            thrustersActive = !thrustersActive
                        },
                        modifier = Modifier.weight(1f)
                    ) { color ->
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val path = Path().apply {
                                moveTo(w * 0.3f, h * 0.35f)
                                lineTo(w * 0.7f, h * 0.35f)
                                lineTo(w * 0.62f, h * 0.6f)
                                lineTo(w * 0.38f, h * 0.6f)
                                close()
                            }
                            drawPath(path, color = Color.White.copy(alpha = 0.7f))

                            if (thrustersActive) {
                                val flameHeight = (h * 0.2f) + (h * 0.15f * flamePhase)
                                val flamePath = Path().apply {
                                    moveTo(w * 0.42f, h * 0.62f)
                                    lineTo(w * 0.5f, h * 0.62f + flameHeight)
                                    lineTo(w * 0.58f, h * 0.62f)
                                    close()
                                }
                                drawPath(flamePath, color = color)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Box 3: Shield Generator
                    val shieldStatus = if (shieldRecharging) "RECHARGING..." else String.format("%.0f%% ACTIVE", shieldCharge * 100f)
                    val shieldColor = when {
                        shieldRecharging -> Color(0xFFFFC107)
                        shieldCharge < 0.3f -> Color(0xFFFF5252)
                        else -> Color(0xFF00E676)
                    }
                    SciFiCard(
                        title = "Shields",
                        statusText = shieldStatus,
                        statusColor = shieldColor,
                        accentColor = Color(0xFF00E676),
                        onClick = {
                            if (!shieldRecharging) {
                                if (shieldCharge >= 0.99f) {
                                    shieldCharge = 0.15f
                                }
                                shieldRecharging = true
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) { color ->
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val shieldPath = Path().apply {
                                moveTo(w * 0.5f, h * 0.25f)
                                quadraticTo(w * 0.75f, h * 0.22f, w * 0.75f, h * 0.45f)
                                quadraticTo(w * 0.75f, h * 0.72f, w * 0.5f, h * 0.84f)
                                quadraticTo(w * 0.25f, h * 0.72f, w * 0.25f, h * 0.45f)
                                quadraticTo(w * 0.25f, h * 0.22f, w * 0.5f, h * 0.25f)
                            }
                            drawPath(
                                path = shieldPath,
                                color = color.copy(alpha = if (shieldRecharging) shieldPulse else 0.8f),
                                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawPath(
                                path = shieldPath,
                                color = color.copy(alpha = (if (shieldRecharging) shieldPulse else 0.25f) * shieldCharge)
                            )
                        }
                    }

                    // Box 4: Warp Engine / Temporal Engine
                    val warpStatus = when (warpFactor) {
                        0 -> "STABILIZED"
                        1 -> "WARP 1.0x"
                        2 -> "WARP 3.5x"
                        else -> "WARP 9.9x"
                    }
                    val warpColor = when (warpFactor) {
                        0 -> Color(0xFF00E676)
                        1 -> Color(0xFFFF9100)
                        2 -> Color(0xFFFF5252)
                        else -> Color(0xFFFF1744)
                    }
                    SciFiCard(
                        title = "Warp Drive",
                        statusText = warpStatus,
                        statusColor = warpColor,
                        accentColor = Color(0xFFFF9100),
                        onClick = {
                            warpFactor = (warpFactor + 1) % 4
                        },
                        modifier = Modifier.weight(1f)
                    ) { color ->
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val strokeWidth = 2.dp.toPx()
                            val activeSpeed = if (warpFactor > 0) temporalPhase else 0f

                            rotate(degrees = activeSpeed, pivot = Offset(w * 0.5f, h * 0.5f)) {
                                drawOval(
                                    color = color.copy(alpha = 0.4f),
                                    topLeft = Offset(w * 0.18f, h * 0.4f),
                                    size = Size(w * 0.64f, h * 0.2f),
                                    style = Stroke(width = strokeWidth)
                                )
                                drawCircle(
                                    color = color,
                                    radius = 3.dp.toPx(),
                                    center = Offset(w * 0.82f, h * 0.5f)
                                )
                            }

                            rotate(degrees = -activeSpeed * 1.5f, pivot = Offset(w * 0.5f, h * 0.5f)) {
                                drawOval(
                                    color = color.copy(alpha = 0.4f),
                                    topLeft = Offset(w * 0.4f, h * 0.18f),
                                    size = Size(w * 0.2f, h * 0.64f),
                                    style = Stroke(width = strokeWidth)
                                )
                                drawCircle(
                                    color = color,
                                    radius = 3.dp.toPx(),
                                    center = Offset(w * 0.5f, h * 0.82f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Diagnostics Console Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0F0E1E).copy(alpha = 0.6f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "QUANTUM LOGS // SECURE_CON",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = when {
                            reactorTemp > 75f -> ">> WARNING: reactor thermal limits approaching critical thresholds"
                            reactorStability < 85f -> ">> CAUTION: micro-gravity variance detected in containment cell"
                            thrustersActive -> ">> INFO: auxiliary plasma jets engaged at full vector thrust"
                            shieldRecharging -> ">> INFO: power grid shunted to magnetic deflectors"
                            else -> ">> STATUS: system idling in standard quantum state. gravity well balanced."
                        },
                        color = when {
                            reactorTemp > 75f -> Color(0xFFFF5252)
                            reactorStability < 85f -> Color(0xFFFFC107)
                            else -> Color(0xFF00E676).copy(alpha = 0.8f)
                        },
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun SciFiCard(
    title: String,
    statusText: String,
    statusColor: Color,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconContent: @Composable (Color) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh),
        label = "scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        accentColor.copy(alpha = 0.6f),
                        accentColor.copy(alpha = 0.1f),
                        accentColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    pressed = true
                    onClick()
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF141324).copy(alpha = 0.75f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        LaunchedEffect(pressed) {
            if (pressed) {
                delay(100)
                pressed = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(accentColor.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                        .border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    iconContent(accentColor)
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(statusColor, RoundedCornerShape(50))
                        .drawBehind {
                            drawCircle(
                                color = statusColor.copy(alpha = 0.4f),
                                radius = size.minDimension * 1.8f
                            )
                        }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title.uppercase(),
                    color = Color.White,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AntiGravityTheme {
        AntiGravityConsole()
    }
}