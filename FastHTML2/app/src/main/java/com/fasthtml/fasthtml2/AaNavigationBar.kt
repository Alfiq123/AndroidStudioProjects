package com.fasthtml.fasthtml2

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LaptopChromebook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object Home : Screen(
        title = "Beranda",
        icon = Icons.Default.Home,
        route = "home"
    )

    object Materi : Screen(
        title = "Materi",
        icon = Icons.Default.Book,
        route = "materi"
    )

    object Praktik : Screen(
        title = "Praktik",
        icon = Icons.Default.Assignment,
        route = "praktik"
    )

    object Latihan : Screen(
        title = "Latihan",
        icon = Icons.Default.LaptopChromebook,
        route = "latihan"
    )

    object Profil : Screen(
        title = "Profil",
        icon = Icons.Default.Person,
        route = "profil"
    )
}

// === MATERI SCREEN === \\
@Composable
fun MateriScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman Materi")
    }
}

// === PRAKTIK SCREEN === \\
@Composable
fun PraktikScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman Praktik")
    }
}

// === LATIHAN SCREEN === \\
@Composable
fun LatihanScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman Latihan")
    }
}

// === PROFIL SCREEN === \\
@Composable
fun ProfilScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Halaman Profil")
    }
}

// ========== MAIN SCREEN ========== \\

@Composable
fun NavigationBar(navController: NavController) {
    val screens = listOf(Screen.Home, Screen.Materi, Screen.Praktik, Screen.Latihan, Screen.Profil)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(route = screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(text = screen.title)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NavigationBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screen.Home.route) { HomeScreen() }
            composable(route = Screen.Materi.route) { MateriScreen() }
            composable(route = Screen.Praktik.route) { PraktikScreen() }
            composable(route = Screen.Latihan.route) { LatihanScreen() }
            composable(route = Screen.Profil.route) { ProfilScreen() }
        }
    }
}