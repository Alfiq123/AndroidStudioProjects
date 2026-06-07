package com.fasthtml.fasthtmlalpha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.fasthtml.fasthtmlalpha.ui.theme.Fast_HTML_AlphaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fast_HTML_AlphaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AllThings(modifier = Modifier.padding(paddingValues = innerPadding))
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

@Composable
fun AllThings(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TheButton()
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        TheTextField()
    }
}

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
fun TheNavBar(modifier: Modifier = Modifier) {

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem()
            }
        }
    ) { innerPadding ->
        NavHost(

        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Fast_HTML_AlphaTheme { AllThings() }
}