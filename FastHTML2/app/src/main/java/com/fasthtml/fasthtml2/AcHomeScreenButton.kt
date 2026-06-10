package com.fasthtml.fasthtml2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun CustomCardLayout() {
    // Kotak luar utama (Container Card)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFF121212), shape = RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Teks paling atas
            Text(
                text = "Text Atas",
                color = Color.White,
                fontSize = 18.sp
            )

            // 2. Baris konten (Icon + Teks & Progress + Teks Kanan)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Komponen Icon (Kotak dengan sudut tumpul)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Icon", color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Kolom tengah (Dua Teks + Progress Bar)
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Text Tengah 1", color = Color.White)
                    Text(text = "Text Tengah 2", color = Color.Gray, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    // Indikator Progress
                    LinearProgressIndicator(
                        progress = { 0.6f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Color.White,
                        trackColor = Color.DarkGray,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 3. Teks di ujung kanan
                Text(
                    text = "Text Kanan",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}
