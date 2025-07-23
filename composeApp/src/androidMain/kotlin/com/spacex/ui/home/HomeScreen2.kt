package com.spacex.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen2(title: String, content: (@Composable () -> Unit)? = null,
) {
    Text(title)
    content?.invoke()
}

