package com.example.bussinesscard

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bussinesscard.ui.theme.BussinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BussinessCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ComposeBusinessCard(modifier = Modifier.padding(paddingValues = innerPadding))
                }
            }
        }
    }
}

@Composable
fun ComposeBusinessCard(modifier: Modifier = Modifier) {
    BusinessCard(
        modifier = modifier,
        img1 = painterResource(id = R.drawable.android_logo),
        text1 = "Jennifer Doe",
        text2 = "Android Developer Extraordinaire",
        numberImg = painterResource(id = R.drawable.phone_call),
        txtNumber = "+11 (123) 444 555 666",
        usernameImg = painterResource(id = R.drawable.share),
        txtUsername = "@AndroidDev",
        emailImg = painterResource(id = R.drawable.email),
        txtEmail = "jen_doe@android.com"
    )
}

@Composable
fun BusinessCard(
    modifier: Modifier = Modifier,
    img1: Painter,
    text1: String,
    text2: String,
    numberImg: Painter,
    txtNumber: String,
    usernameImg: Painter,
    txtUsername: String,
    emailImg: Painter,
    txtEmail: String
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFD2E8D4),
        contentColor = Color(0xFF003D1D)
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = img1, contentDescription = null, modifier = Modifier.size(100.dp))
            Text(text = text1, fontSize = 48.sp)
            Text(text = text2)
        }

        Column(
            modifier = modifier.fillMaxSize().padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Row( // NoTelp
                    modifier = Modifier.padding(all = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = numberImg,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 12.dp)
                    )
                    Text(text = txtNumber)
                }
                Row( // Username
                    modifier = Modifier.padding(all = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = usernameImg,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 12.dp)
                    )
                    Text(text = txtUsername)
                }
                Row( // Email
                    modifier = Modifier.padding(all = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = emailImg,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 12.dp)
                    )
                    Text(text = txtEmail)
                }
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Composable
fun GreetingPreview() {
    BussinessCardTheme {
        ComposeBusinessCard()
    }
}