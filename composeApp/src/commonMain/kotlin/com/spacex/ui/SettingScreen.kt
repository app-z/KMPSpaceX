package com.spacex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.AppPreferences.Companion.ROW_MODE
import com.spacex.viewmodel.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    rootNavController: NavController,
    paddingValues: PaddingValues
) {


    val viewModel = koinViewModel<SettingsViewModel>()
    val rowModeState = viewModel.modeState.collectAsStateWithLifecycle()

//    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
//    val rowModeState = viewModel.modeState
//        .flowWithLifecycle(
//            lifecycleOwner.lifecycle,
//            Lifecycle.State.STARTED
//        ).collectAsState(
//            context = lifecycleOwner.lifecycleScope.coroutineContext,
//            initial = null
//        )

    SettingScreenContent(
        paddingValues, rowModeState, onSetRowMode =
            { mode ->
                viewModel.setRowModeState(mode)
            }
    )

}

@Composable
fun SettingScreenContent(
    paddingValues: PaddingValues,
    rowModeState: State<String?>,
    onSetRowMode: (rowMode: String) -> Unit
) {
    Column(
        modifier = Modifier.padding(paddingValues)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Setting",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier.padding(paddingValues)
                .fillMaxWidth()
        ) {

            Text(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f),
                text = "Card mode"
            )

            Checkbox(
                checked = rowModeState.value == CARD_MODE,
                onCheckedChange = {
                    onSetRowMode.invoke(if (it) CARD_MODE else ROW_MODE)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
//            rootNavController.navigate(Routes.SettingDetail.route)

}


@Preview
@Composable
fun PreviewSettings1() {
    SettingScreenContent(
        paddingValues = PaddingValues(),
        rowModeState = mutableStateOf<String>(CARD_MODE),
        onSetRowMode = {}
    )
}

@Preview
@Composable
fun PreviewSettings2() {
    SettingScreenContent(
        paddingValues = PaddingValues(),
        rowModeState = mutableStateOf<String>(ROW_MODE),
        onSetRowMode = {}
    )
}

//@Preview
//@Composable
//fun MyScreenPreview() {
//    PreviewSettings {
//        SettingScreen(
//            rootNavController = rememberNavController(),
//            paddingValues = PaddingValues(16.dp)
//        )
//    }
//}
//
//
//@Composable
//fun PreviewSettings(
//    screen: @Composable () -> Unit
//) {
//
//    KoinApplication(application = {
//        modules(
//            databaseModule,
//            networkModule,
//            repositoryModule,
//            viewmodelModule
//        )
//    }) {
//
//
//        screen()

//        val rootNavController = rememberNavController()
//
//        SettingScreen(
//            rootNavController = rootNavController,
//            paddingValues = PaddingValues(16.dp)
//        )
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDetailScreen(
    rootNavController: NavController,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Setting Detail",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                IconButton(onClick = { rootNavController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        )
    }
}