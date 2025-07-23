package com.spacex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.spacex.model.FalconInfo
import com.spacex.model.FalconUiState
import com.spacex.navigation.Routes
import com.spacex.ui.common.EmptyContent
import com.spacex.ui.common.NetworkError
import com.spacex.ui.common.ShimmerEffect
import com.spacex.utils.AppPreferences.Companion.CARD_MODE
import com.spacex.utils.AppPreferences.Companion.ROW_MODE
import com.spacex.utils.NetworkResponse
import com.spacex.viewmodel.FavoriteViewModel
import com.spacex.viewmodel.IFalconsViewModel
import com.spacex.viewmodel.MainViewModel
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_browse
import kmpspacex.composeapp.generated.resources.no_data
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FalconScreen(
    rootNavController: NavController,
    paddingValues: PaddingValues,
    isFavorite: Boolean
) {

    val viewModel = if (isFavorite) {
        koinViewModel<FavoriteViewModel>()
    } else {
        koinViewModel<MainViewModel>()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val windowSizeClass = calculateWindowSizeClass()

    val isMediumExpandedWWSC by remember(windowSizeClass) {
        derivedStateOf {
            windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
    }

    val isFindActive = remember { mutableStateOf(false) }
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

    Column(modifier = Modifier
        .padding(bottom = paddingValues.calculateBottomPadding())) {
        TopAppBar(
            title = {
                if (isFindActive.value) {
                    Column() {
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
                }
            },
            actions = {
                IconButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterVertically),
                    onClick = {
                        isFindActive.value = !isFindActive.value
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

        when (uiState.networkResponse) {
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
                val falconInfos =
                    (uiState.networkResponse as NetworkResponse.Success<List<FalconInfo>>).data
                FalconInfoListView(
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
                    rowMode = uiState.rowMode
                )
            }

            NetworkResponse.Empty ->
                EmptyContent(
                    message = stringResource(Res.string.no_data),
                    icon = Res.drawable.ic_browse,
                    onRetryClick = {
                        updateFalcons(findText, viewModel)
                    }
                )
        }
    }
}

@Composable
fun FalconInfoListView(
    falconInfos: List<FalconInfo>?,
    paddingValues: PaddingValues,
    onDetail: (FalconInfo) -> Unit,
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
                                onClick = {
                                    onDetail.invoke(falconInfos[index])
                                })
                        }
                    })
            }

            ROW_MODE -> {

                LazyColumn(
//                    modifier = Modifier.padding(paddingValues)
                ) {
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
    viewModel: IFalconsViewModel<FalconUiState>
) {
    if (filter.isEmpty()) {
        viewModel.getFalcons()
    } else {
        viewModel.getFilteredFalcons(filter)
    }
    println("filter = $filter")

}


