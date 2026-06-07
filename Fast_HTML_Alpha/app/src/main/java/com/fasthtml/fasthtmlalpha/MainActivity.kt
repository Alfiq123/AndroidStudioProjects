package com.fasthtml.fasthtmlalpha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Abc
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fasthtml.fasthtmlalpha.ui.theme.Fast_HTML_AlphaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fast_HTML_AlphaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationBarExample(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : Screen(
        route = "home",
        title = "Home",
        Icons.Default.Home
    )

    object Search : Screen(
        route = "search",
        title = "Search",
        Icons.Default.Search
    )

    object Profile : Screen(
        route = "profile",
        title = "Profile",
        Icons.Default.Person
    )
}

// @Composable
// fun AllThings(modifier: Modifier = Modifier) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TheButton()
//        Spacer(modifier = Modifier.padding(vertical = 10.dp))
//        TheTextField()
//        Spacer(modifier = Modifier.padding(vertical = 10.dp))
//        NavigationBarExample()
//    }
// }

@Composable
fun TheButton(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* TODO */ } /* enabled = false */) {
            Icon(
                imageVector = Icons.Outlined.NoAccounts,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Create a new account")
        }

        OutlinedButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "I have an existing account")
        }

        TextButton(
            onClick = { /* TODO */ }, shape = CutCornerShape(size = 8.dp)
        ) {
            Text(text = "Text Button")
        }

        FilledTonalButton(onClick = { /* TODO */ }) {
            Icon(
                imageVector = Icons.Outlined.OpenInBrowser,
                contentDescription = "Open in Browser",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Open in Browser")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ElevatedButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Add to Cart",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add to Cart")
            }

            OutlinedButton(onClick = { /* TODO */ }) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Add to Cart",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Back")
            }
        }
    }
}

@Composable
fun TheTextField(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var filledText by remember() { mutableStateOf(value = "") }
        TextField(
            value = filledText,
            onValueChange = {
                filledText = it
            },
            // enabled = true,
            // readOnly = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Left),
            label = {
                Text(text = "Enter Something")
            },
            placeholder = {
                Text(text = "eg: I'm Cool")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Abc,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Accessibility,
                    contentDescription = null,
                )
            },
            // prefix = {
            //     Text(text = "temp@")
            // },
            // suffix = {
            //     Text(text = ".com")
            // },
            supportingText = {
                Text(text = "*Required")
            },
            isError = false,
            // visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    println("Next Next")
                }
            ),
            maxLines = 1,
            minLines = 1,
        )

        Spacer(modifier = Modifier.padding(vertical = 5.dp))

        var outlinedText by remember() { mutableStateOf(value = "") }
        OutlinedTextField(
            value = outlinedText,
            onValueChange = {
                outlinedText = it
            },
            // enabled = true,
            // readOnly = true,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Left),
            label = {
                Text(text = "Enter Something")
            },
            placeholder = {
                Text(text = "eg: I'm Cool")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Abc,
                    contentDescription = null,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Accessibility,
                    contentDescription = null,
                )
            },
            // prefix = {
            //     Text(text = "temp@")
            // },
            // suffix = {
            //     Text(text = ".com")
            // },
            supportingText = {
                Text(text = "*Required")
            },
            isError = false,
            // visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    println("Next Next")
                }
            ),
            maxLines = 1,
            minLines = 1,
        )
    }
}


@Composable
fun SongsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Text("Songs Screen")
        TheButton()
    }
}

@Composable
fun AlbumScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Text("Album Screen")
        TheTextField()
    }
}

@Composable
fun PlaylistScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Playlist Screen")
    }
}

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    SONGS(
        route = "songs",
        label = "Songs",
        Icons.Default.MusicNote,
        contentDescription =  "Songs"
    ),
    ALBUM(
        route = "album",
        label = "Album",
        Icons.Default.Album,
        contentDescription = "Album"
    ),
    PLAYLISTS(
        route = "playlist",
        label = "Playlist",
        Icons.Default.PlaylistAddCircle,
        contentDescription = "Playlist"
    )
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach {
            destination -> composable(destination.route) {
                when (destination) {
                    Destination.SONGS -> SongsScreen()
                    Destination.ALBUM -> AlbumScreen()
                    Destination.PLAYLISTS -> PlaylistScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationBarExample(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.SONGS
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(value = startDestination.ordinal)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(
            navController,
            startDestination,
            modifier = Modifier.padding(paddingValues = contentPadding)
        )
    }
}

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview() { Fast_HTML_AlphaTheme { SongsScreen() } }

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview2() { Fast_HTML_AlphaTheme { AlbumScreen() } }

// @Preview(showBackground = true)
// @Composable
// fun GreetingPreview3() { Fast_HTML_AlphaTheme { PlaylistScreen() } }
