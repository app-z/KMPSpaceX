package com.spacex.presentation.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.AppPreferences.Companion.ROW_MODE
import com.spacex.utils.Theme
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.title_settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    rootNavController: NavController,
    snackBarHostState: SnackbarHostState,
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

//    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//    val rowModeState = viewModel.modeState
//        .flowWithLifecycle(
//            lifecycleOwner.lifecycle,
//            Lifecycle.State.STARTED
//        ).collectAsState(
//            context = lifecycleOwner.lifecycleScope.coroutineContext,
//            initial = null
//        )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.title_settings),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        }
    ) { paddingValues ->

        SettingScreenContent(
            paddingValues = paddingValues,
            state = state,
            onSetRowMode =
                { mode ->
                    viewModel.handleEvent(
                        SettingsEvent.RowMode(mode)
                    )
                },
            onChangeTheme = { themeDark ->
                viewModel.handleEvent(
                    SettingsEvent.Theme(
                        if (themeDark) {
                            Theme.DARK_MODE.name
                        } else {
                            Theme.LIGHT_MODE.name
                        }
                    )
                )
            }
        )
    }
}

@Composable
fun SettingScreenContent(
    paddingValues: PaddingValues,
    state: State<SettingsState>,
    onSetRowMode: (rowMode: String) -> Unit,
    onChangeTheme: (themeDark: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.padding(paddingValues)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = "Card mode"
            )

            Checkbox(
                checked = state.value.rowMode == CARD_MODE,
                onCheckedChange = {
                    onSetRowMode.invoke(if (it) CARD_MODE else ROW_MODE)
                },
                modifier = Modifier.padding(16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = "Dark Theme"
            )

            Checkbox(
                checked = state.value.currentTheme == Theme.DARK_MODE.name,
                onCheckedChange = {
                    onChangeTheme.invoke(it)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettings1() {
    SettingScreenContent(
        paddingValues = PaddingValues(),
        state = mutableStateOf<SettingsState>(
            SettingsState(
                currentTheme = Theme.DARK_MODE.name,
                rowMode = CARD_MODE
            )
        ),
        onSetRowMode = {},
        onChangeTheme = {}
    )
}

@Preview
@Composable
fun PreviewSettings2() {
    SettingScreenContent(
        paddingValues = PaddingValues(),
        state = mutableStateOf<SettingsState>(
            SettingsState(
                currentTheme = Theme.LIGHT_MODE.name,
                rowMode = ROW_MODE
            )
        ),
        onSetRowMode = {},
        onChangeTheme = {}
    )
}
