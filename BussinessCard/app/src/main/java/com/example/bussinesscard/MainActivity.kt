package com.example.bussinesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        text1 = "Jennifer Doe",
        text2 = "Android Developer Extraordinaire",
        txtNumberImg = "Image:  ",
        txtNumber = "+11 (123) 444 555 666",
        txtUsernameImg = "Image:  ",
        txtUsername = "@AndroidDev",
        txtEmailImg = "Image:  ",
        txtEmail = "jen_doe@android.com"
    )
}

@Composable
fun BusinessCard(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String,
    txtNumberImg: String,
    txtNumber: String,
    txtUsernameImg: String,
    txtUsername: String,
    txtEmailImg: String,
    txtEmail: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD2E8D4))
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = text1)
            Text(text = text2)
        }

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(all = 10.dp)
            ) {
                Row(modifier = Modifier.padding(all = 2.dp)) {
                    Text(text = txtNumberImg)
                    Text(text = txtNumber)
                }
                Row(modifier = Modifier.padding(all = 2.dp)) {
                    Text(text = txtUsernameImg)
                    Text(text = txtUsername)
                }
                Row(modifier = Modifier.padding(all = 2.dp)) {
                    Text(text = txtEmailImg)
                    Text(text = txtEmail)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BussinessCardTheme {
        ComposeBusinessCard()
    }
}