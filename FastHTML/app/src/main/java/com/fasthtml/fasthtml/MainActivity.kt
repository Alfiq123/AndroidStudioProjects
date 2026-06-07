package com.fasthtml.fasthtml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fasthtml.fasthtml.ui.theme.FastHTMLTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FastHTMLTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainAppScreen()
                }
            }
        }
    }
}
