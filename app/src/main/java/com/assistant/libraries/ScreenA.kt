package com.assistant.libraries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class ScreenA : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Greeting()
            }
        }
    }
}

@Composable
fun Greeting() {
    Text(text = "Hello, Library Lighthouse!")
}

@Preview(showBackground = true)
@Composable
fun PreviewGreeting() {
    MaterialTheme {
        Greeting()
    }
}
