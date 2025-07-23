package com.spacex

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spacex.theme.FalconsTheme
import com.spacex.ui.MainScreen
import com.spacex.presentation.settings.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    KoinContext {
        val settingViewModel = koinViewModel<SettingsViewModel>()
        val currentTheme by settingViewModel.state.collectAsStateWithLifecycle()
        FalconsTheme(currentTheme.currentTheme) {
            MainScreen(settingViewModel)
        }
    }
}
