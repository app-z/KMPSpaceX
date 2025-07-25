package com.spacex.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spacex.model.FalconInfo
import com.spacex.navigation.Routes
import com.spacex.ui.common.EmptyContent
import com.spacex.ui.common.NetworkError
import com.spacex.ui.common.ShimmerEffect
import com.spacex.utils.NetworkResponse
import com.spacex.viewmodel.MainViewModel
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_browse
import kmpspacex.composeapp.generated.resources.no_data
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.text.set

@Composable
fun FalconScreen(
    rootNavController: NavController,
    paddingValues: PaddingValues
) {

    val viewModel = koinViewModel<MainViewModel>()

    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is NetworkResponse.Error -> NetworkError(onRetryButtonClick = {
            viewModel.getFalcons()
        })

        is NetworkResponse.Idle -> {}
        is NetworkResponse.Loading -> {
            ShimmerEffect()
        }
        is NetworkResponse.Success -> {
            val falconInfos = (uiState as NetworkResponse.Success<List<FalconInfo>>).data
            FalconInfoTwoRowListView(
                falconInfos = falconInfos, paddingValues = paddingValues,
                onRetry = {
                    viewModel.getFalcons()
                },
                onDetail = { falconInfo ->

                rootNavController.currentBackStackEntry?.savedStateHandle?.apply {
                        val jsonFalconInfo = Json.encodeToString(falconInfo)
                        set("name", jsonFalconInfo)
                    }
                    rootNavController.navigate(Routes.FalconsDetail.route)
                })
        }
    }
}

@Composable
fun FalconInfoTwoRowListView(
    falconInfos: List<FalconInfo>?,
    paddingValues: PaddingValues,
    onDetail: (FalconInfo) -> (Unit),
    onRetry: () -> (Unit)
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
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
}


@Composable
fun FalconInfoItem(
    item: FalconInfo,
    onAddToCart: (fruittie: FalconInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(onClick = { onAddToCart(item) }) {
                Text(
                    "Add"
                    //stringResource(R.string.add)
                )
            }
        }
    }
}

