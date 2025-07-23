package com.spacex

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.spacex.ui.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
        MaterialTheme {
            MainScreen()
        }
}