package com.spacex.presentation.falcons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FindInPage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.spacex.model.FalconInfo
import com.spacex.ui.FalconInfoCard2
import com.spacex.ui.FalconInfoRow
import com.spacex.ui.common.EmptyContent
import com.spacex.ui.common.ShimmerEffect
import com.spacex.ui.home.NetworkError
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.AppPreferences.Companion.ROW_MODE
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.falcons
import kmpspacex.composeapp.generated.resources.ic_browse
import kmpspacex.composeapp.generated.resources.no_data
import kmpspacex.composeapp.generated.resources.title_settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FalconsScreen(
    rootNavController: NavController,
    snackbarHostState: SnackbarHostState,
    onNavigateToDetails: (falconsInfo: FalconInfo) -> Unit,
    paddingValues: PaddingValues
) {

    val viewModel = koinViewModel<FalconsViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val windowSizeClass = calculateWindowSizeClass()

    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }

    var findText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        snapshotFlow {
            findText
        }
            .distinctUntilChanged()
            .debounce(500)
            .collectLatest {
                updateFalcons(it, viewModel)
            }
    }

    val scope = rememberCoroutineScope()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val effect = viewModel.effect
        .flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )



    SideEffect {
        println("uiState = $uiState")
        println("effect = $effect")
    }

    val localLifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = localLifecycleOwner) {
        effect.collect {
            when (it) {
                is FalconsEffect.NavigateToDetails -> {
                    onNavigateToDetails(it.falconInfo)
                }

                is FalconsEffect.ShowError -> {
                    scope.launch(Dispatchers.Main) {
                        snackbarHostState.showSnackbar(it.message.asStringForSuspend())
                    }
                }

                is FalconsEffect.ShowSuccess -> {
                    scope.launch(Dispatchers.Main) {
                        snackbarHostState.showSnackbar(it.message.asStringForSuspend())
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState.isFindActive) {
                        Column {
                            TextField(
                                value = findText,
                                onValueChange = { newValue ->
                                    findText = newValue
                                },
                                singleLine = true,
                                placeholder = { Text("Falcon...") },
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.SansSerif
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    } else {
                        findText = ""
                        Text(
                            text = stringResource(Res.string.falcons),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically),
                        onClick = {
                            viewModel.handleEvent(FalconsEvent.ToggleFindActive)
                        }) {
                        Icon(
//                        modifier = Modifier
//                            .fillMaxHeight(),
                            imageVector = Icons.Outlined.FindInPage,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ContentScreen(uiState, viewModel, isMediumExpandedWWSC)
        }
    }
}

@Composable
fun ContentScreen(
    uiState: FalconsState,
    viewModel: FalconsViewModel,
    isMediumExpandedWWSC: Boolean
) {
    when {
        uiState.isLoading -> {
            ShimmerEffect(if (isMediumExpandedWWSC) 4 else 2)
        }

        uiState.error != null -> {
            NetworkError(
                onRetry = {
                    viewModel.loadFalcons()
                }
            )
        }

        uiState.falconInfos.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyContent(
                    message = stringResource(Res.string.no_data),
                    icon = Res.drawable.ic_browse,
                    onRetryClick = {
                    }
                )
            }
        }

        else -> {
            FalconInfoListView(
                falconInfos = uiState.falconInfos,
                isMediumExpandedWWSC = isMediumExpandedWWSC,
                onRetry = {
                    viewModel.loadFalcons()
                },
                onDetail = { falconInfo ->
                    viewModel.handleEvent(
                        FalconsEvent.NavigateToFalconInfoDetails(
                            falconInfo
                        )
                    )
                },
                onFavorite = { falconInfos ->
                    viewModel.handleEvent(FalconsEvent.ToggleFavorites(falconInfos))
                },
                rowMode = uiState.rowMode
            )
        }
    }
}


@Composable
fun FalconInfoListView(
    falconInfos: List<FalconInfo>?,
    onDetail: (FalconInfo) -> Unit,
    onFavorite: (FalconInfo) -> Unit,
    onRetry: () -> Unit,
    rowMode: String,
    isMediumExpandedWWSC: Boolean,
) {


    if (falconInfos.isNullOrEmpty()) {
        EmptyContent(
            message = stringResource(Res.string.no_data),
            icon = Res.drawable.ic_browse,
            onRetryClick = {
                onRetry.invoke()
            }
        )
    } else {

        when (rowMode) {

            CARD_MODE -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(if (isMediumExpandedWWSC) 4 else 2),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                    contentPadding = paddingValues,
                    content = {
                        items(falconInfos.size) { index ->
                            FalconInfoCard2(
                                falconInfos[index],
                                index,
                                onClickItem = {
                                    onDetail.invoke(falconInfos[index])
                                },
                                onClickFavorite = {
                                    onFavorite.invoke(falconInfos[index])
                                }
                            )
                        }
                    })
            }

            ROW_MODE -> {

                LazyColumn {
                    items(falconInfos, key = { it.id }) { falconInfo ->
                        FalconInfoRow(
                            falconInfo = falconInfo,
                            onClick = {
                                onDetail.invoke(falconInfo)
                            })

                    }
                }
            }
        }
    }
}

private fun updateFalcons(
    filter: String,
    viewModel: IFalconsViewModel<FalconsState>
) {
    if (filter.isEmpty()) {
        viewModel.loadFalcons()
    } else {
        viewModel.getFilteredFalcons(filter)
    }
    println("filter = $filter")

}


