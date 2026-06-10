package com.fasthtml.fasthtml

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home
    )
    object Study : Screen(
        route = "study",
        title = "Study",
        icon = Icons.Filled.Book
    )
    object Practice : Screen(
        route = "practice",
        title = "Practice",
        icon = Icons.Filled.Assignment
    )
    object Profile : Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Filled.Person
    )
    object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Filled.Settings
    )
}

@Composable
fun HomeScreen(onCategoryClick: (String) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        KategoriMateri(onCategoryClick = onCategoryClick)
    }
}

@Composable
fun StudyScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularDeterminateIndicator()
    }
}

@Composable
fun PracticeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        IndeterminateLinearIndicator()
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        IndeterminateCircularIndicator()
    }
}

@Composable
fun SettingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LinearProgressIndicator()
    }
}


@Composable
fun NavigationIcon(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(durationMillis = 500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(durationMillis = 500)
            )
        }
    ) {
        composable(Screen.Home.route) { 
            HomeScreen(onCategoryClick = { route -> navController.navigate(route) }) 
        }
        composable(Screen.Study.route) { StudyScreen() }
        composable(Screen.Practice.route) { PracticeScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
        composable(Screen.Settings.route) { SettingScreen() }

        // Route untuk Kategori
        composable(route = "dasar") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Dasar HTML")
            }
        }
        composable(route = "elemen") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Elemen")
            }
        }
        composable(route = "attribut") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Attribut")
            }
        }
        composable(route = "tabel") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Tabel")
            }
        }
        composable(route = "form") {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Halaman Form")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Study, Screen.Practice, Screen.Profile, Screen.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(imageVector = screen.icon, contentDescription = screen.title)
                        },
                        label = {
                            Text(text = screen.title)
                        },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavigationIcon(
            navController = navController,
            modifier = Modifier.padding(paddingValues = innerPadding)
        )
    }
}
