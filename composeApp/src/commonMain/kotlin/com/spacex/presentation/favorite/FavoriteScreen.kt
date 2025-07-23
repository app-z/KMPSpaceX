package com.spacex.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.spacex.model.FalconInfo
import com.spacex.ui.FalconInfoCard2
import com.spacex.ui.FalconInfoRow
import com.spacex.ui.common.EmptyContent
import com.spacex.ui.common.NetworkError
import com.spacex.ui.common.ShimmerEffect
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.AppPreferences.Companion.ROW_MODE
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_browse
import kmpspacex.composeapp.generated.resources.no_data
import kmpspacex.composeapp.generated.resources.title_favorites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    rootNavController: NavController,
    snackbarHostState: SnackbarHostState,
    onNavigateToDetails: (FalconInfo) -> Unit,
    paddingValues: PaddingValues
) {

    val viewModel = koinViewModel<FavoriteViewModel>()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    val lifecycleOwner = LocalLifecycleOwner.current

    val effect = viewModel.effect
        .flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )

    val windowSizeClass = calculateWindowSizeClass()

    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }

    val scope = rememberCoroutineScope()



    LaunchedEffect(lifecycleOwner.lifecycle) {
        // Handle effects
        viewModel.effect.collect {

            when (it) {
                is FavoritesEffect.NavigateToDetails -> {
                    onNavigateToDetails(it.falconInfo)
                }

                is FavoritesEffect.ShowError -> {
                    scope.launch(Dispatchers.Main) {
                        snackbarHostState.showSnackbar(it.message.asStringForSuspend())
                    }
                }

                is FavoritesEffect.ShowSuccess -> {
                    scope.launch(Dispatchers.Main) {
                        snackbarHostState.showSnackbar(it.message.asStringForSuspend())
                    }
                }

                FavoritesEffect.Initial -> println(">>> Initial")
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.title_favorites),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                },
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when {
                uiState.isLoading -> {
                    ShimmerEffect(if (isMediumExpandedWWSC) 4 else 2)
                }

                uiState.error != null -> {
                    NetworkError(
                        message = uiState.error!!.asString(), onRetry = {
                            viewModel.handleEvent(FavoritesEvent.LoadFavorites)
                        })
                }

                uiState.favoritesFalconInfos.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        EmptyContent(
                            message = stringResource(Res.string.no_data),
                            icon = Res.drawable.ic_browse,
                            onRetryClick = {})
                    }
                }

                else -> {

                    FalconInfoListOrCardOrEmpty(
                        rootNavController = rootNavController,
                        uiState = uiState,
                        falconInfos = uiState.favoritesFalconInfos,
                        onDetail = { falconInfo ->
                            viewModel.handleEvent(
                                FavoritesEvent.NavigateToFalconInfoDetails(
                                    falconInfo
                                )
                            )
                        },
                        onRetry = {
                            viewModel.handleEvent(FavoritesEvent.LoadFavorites)
                        },
                        onFavorite = { falconInfo ->
                            viewModel.handleEvent(FavoritesEvent.ToggleFavorites(falconInfo))
                        },
                        isMediumExpandedWWSC = isMediumExpandedWWSC,
                    )

                }
            }
        }

    }

    SideEffect {
        println(">>> uiState = ${uiState}")
        println(">>> effect = ${effect}")
    }
}


@Composable
fun FalconInfoListOrCardOrEmpty(
    rootNavController: NavController,
    uiState: FavoritesState,
    falconInfos: List<FalconInfo>,
    onDetail: (FalconInfo) -> Unit,
    onRetry: () -> Unit,
    onFavorite: (FalconInfo) -> Unit,
    isMediumExpandedWWSC: Boolean,
) {
    if (uiState.favoritesFalconInfos.isEmpty()) {
        EmptyContent(
            message = stringResource(Res.string.no_data),
            icon = Res.drawable.ic_browse,
            onRetryClick = {
//                        viewModel.getFalcons()
            })
    } else {

        FalconInfoListOrCardView(
            falconInfos = uiState.favoritesFalconInfos,
            isMediumExpandedWWSC = isMediumExpandedWWSC,
            onRetry = {
//                        viewModel.getFalcons()
            },
            onDetail = { falconInfo ->
                onDetail.invoke(falconInfo)
            },
            onFavorite = { falconInfo ->
                onFavorite.invoke(falconInfo)
            },
            rowMode = uiState.rowMode
        )
    }
}

@Composable
fun FalconInfoListOrCardView(
    falconInfos: List<FalconInfo>,
    onDetail: (FalconInfo) -> Unit,
    onRetry: () -> Unit,
    onFavorite: (falconInfo: FalconInfo) -> Unit,
    rowMode: String,
    isMediumExpandedWWSC: Boolean,
) {
    when (rowMode) {

        CARD_MODE -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isMediumExpandedWWSC) 4 else 2),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(falconInfos.size) { index ->
                        FalconInfoCard2(falconInfos[index], index, onClickItem = {
                            onDetail.invoke(falconInfos[index])
                        }, onClickFavorite = {
                            onFavorite.invoke(falconInfos[index])
                        })
                    }
                })
        }

        ROW_MODE -> {

            LazyColumn {
                items(falconInfos, key = { it.id }) { falconInfo ->
                    FalconInfoRow(
                        falconInfo = falconInfo, onClick = {
                            onDetail.invoke(falconInfo)
                        })

                }
            }
        }
    }
}

