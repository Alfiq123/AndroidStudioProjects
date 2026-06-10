package com.fasthtml.fasthtml2

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attribution
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// === HOME SCREEN === \\
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Heading()
        Spacer(modifier = Modifier.padding(vertical = 12.dp))
        ProgressBelajar()
        Spacer(modifier = Modifier.padding(vertical = 12.dp))
        MateriSelanjutnya()
        Spacer(modifier = Modifier.padding(vertical = 12.dp))
        KategoriMateriApp()
    }
}

@Composable
fun Heading() {
    Text(text = "Halo, [ User ]", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.padding(vertical = 2.dp))
    Text(text = "Mari lanjutkan perjalanan belajar HTML kamu!")
}

@Composable
fun ProgressBelajar() {
    Column(
        modifier = Modifier
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(size = 8.dp))
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Progress Belajar", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            CircularProgressIndicator()
            Text(text = "Cek")

            Column(
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
            ) {
                Text(text = "12 / 8")

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Text(text = "6 / 15")
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Lanjutkan")
            }
        }
    }
}

@Composable
fun MateriSelanjutnya() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Text(text = "Materi Selanjutnya", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.padding(vertical = 4.dp))

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.SkipNext,
                contentDescription = "Materi Selanjutnya",
                modifier = Modifier.size(48.dp)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Struktur Dasar HTML", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "HTML Dasar")
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                LinearProgressIndicator()
            }

            Text(text = "50%")
        }
    }
}

sealed class ScreenButtons(val route: String, val label: String, val icon: ImageVector) {
    object HalamanDasar : ScreenButtons(
        route = "halaman_dasar",
        label = "Dasar",
        icon = Icons.Filled.Code
    )
    object HalamanElemen : ScreenButtons(
        route = "halaman_elemen",
        label = "Elemen",
        icon = Icons.Filled.CheckBoxOutlineBlank
    )
    object HalamanAtribut : ScreenButtons(
        route = "halaman_atribut",
        label = "Atribut",
        icon = Icons.Filled.Attribution
    )
    object HalamanTabel : ScreenButtons(
        route = "halaman_tabel",
        label = "Tabel",
        icon = Icons.Filled.TableChart
    )
    object HalamanForm : ScreenButtons(
        route = "halaman_form",
        label = "Form",
        icon = Icons.Filled.FormatLineSpacing
    )
}

@Composable
fun KategoriMateri(navController: NavController) {
    val pages = listOf(
        ScreenButtons.HalamanDasar,
        ScreenButtons.HalamanElemen,
        ScreenButtons.HalamanAtribut,
        ScreenButtons.HalamanTabel,
        ScreenButtons.HalamanForm
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        pages.forEach { page ->
            Column(
                modifier = Modifier.wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(page.route) },
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(size = 8.dp),
                    contentPadding = PaddingValues(all = 0.dp)
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = page.label)
            }
        }
    }
}

@Composable
fun KategoriMateriScreen(title: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(onClick = { navController.popBackStack() }) { Text("← Back to Menu") }
    }
}

@Composable
fun KategoriMateriApp() {
    val navController = rememberNavController()

    Box(modifier = Modifier.border(
        width = 2.dp,
        color = Color.Black,
        shape = RoundedCornerShape(size = 8.dp)
    )) {
        NavHost(
            navController = navController,
            startDestination = "dasar"
        ) {
            composable(route = "dasar") {
                KategoriMateri(navController)
            }

            composable(route = ScreenButtons.HalamanDasar.route) {
                KategoriMateriScreen(
                    title = "Halaman Dasar",
                    navController = navController
                )
            }
            composable(route = ScreenButtons.HalamanElemen.route) {
                KategoriMateriScreen(
                    title = "Halaman Elemen",
                    navController = navController
                )
            }
            composable(route = ScreenButtons.HalamanAtribut.route) {
                KategoriMateriScreen(
                    title = "Halaman Atribut",
                    navController = navController
                )
            }
            composable(route = ScreenButtons.HalamanTabel.route) {
                KategoriMateriScreen(
                    title = "Halaman Tabel",
                    navController = navController
                )
            }
            composable(route = ScreenButtons.HalamanForm.route) {
                KategoriMateriScreen(
                    title = "Halaman Form",
                    navController = navController
                )
            }
        }
    }
}
