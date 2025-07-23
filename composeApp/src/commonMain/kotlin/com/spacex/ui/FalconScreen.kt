package com.spacex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spacex.model.FalconInfo
import com.spacex.navigation.Routes
import com.spacex.ui.common.EmptyContent
import com.spacex.ui.common.NetworkError
import com.spacex.ui.common.ShimmerEffect
import com.spacex.utils.AppPreferences
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.AppPreferences.Companion.ROW_MODE
import com.spacex.utils.NetworkResponse
import com.spacex.viewmodel.MainViewModel
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_browse
import kmpspacex.composeapp.generated.resources.no_data
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FalconScreen(
    rootNavController: NavController,
    paddingValues: PaddingValues
) {

    val appPreferences = koinInject<AppPreferences>()


    val viewModel = koinViewModel<MainViewModel>()

    val uiState by viewModel.uiState.collectAsState()

    val rowMode = remember { mutableStateOf<String>(CARD_MODE) }

    val windowSizeClass = calculateWindowSizeClass()
    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }

    LaunchedEffect(Unit) {
        rowMode.value = appPreferences.getRowMode()
    }

    when (uiState) {
        is NetworkResponse.Error -> NetworkError(
            onRetryButtonClick = {
                viewModel.getFalcons()
            }
        )

        is NetworkResponse.Idle -> {}
        is NetworkResponse.Loading -> {
            ShimmerEffect(if (isMediumExpandedWWSC) 4 else 2)
        }

        is NetworkResponse.Success -> {
            val falconInfos = (uiState as NetworkResponse.Success<List<FalconInfo>>).data
            FalconInfoTwoRowListView(
                falconInfos = falconInfos,
                isMediumExpandedWWSC = isMediumExpandedWWSC,
                paddingValues = paddingValues,
                onRetry = {
                    viewModel.getFalcons()
                },
                onDetail = { falconInfo ->

                    rootNavController.currentBackStackEntry?.savedStateHandle?.apply {
                        val jsonFalconInfo = Json.encodeToString(falconInfo)
                        set("name", jsonFalconInfo)
                    }
                    rootNavController.navigate(Routes.FalconsDetail.route)
                },
                rowMode = rowMode
            )
        }
    }
}

@Composable
fun FalconInfoTwoRowListView(
    falconInfos: List<FalconInfo>?,
    paddingValues: PaddingValues,
    onDetail: (FalconInfo) -> Unit,
    onRetry: () -> Unit,
    rowMode: MutableState<String>,
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

        when (rowMode.value) {

            CARD_MODE -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(if (isMediumExpandedWWSC) 4 else 2),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = paddingValues,
                    content = {
                        items(falconInfos.size) { index ->
                            FalconInfoCard2(
                                falconInfos[index],
                                index,
                                onClick = {
                                    onDetail.invoke(falconInfos[index])
                                })
                        }
                    })
            }

            ROW_MODE -> {

                LazyColumn(modifier = Modifier.padding(paddingValues)) {
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

