package com.example.strikeinn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.strikeinn.presentation.App
import com.example.strikeinn.ui.theme.StrikeINNTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StrikeINNTheme {
                App()
            }
        }
    }
}