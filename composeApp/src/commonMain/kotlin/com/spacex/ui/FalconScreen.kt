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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.spacex.model.FalconInfo
import com.spacex.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FalconScreen(
    rootNavController: NavController,
    paddingValues: PaddingValues) {

    val viewModel = koinViewModel<MainViewModel>()

    val uiState by viewModel.uiState.collectAsState()

    FalconInfoTwoRowListView(uiState.falconInfo, paddingValues)

    if (uiState.error > 0) {
        NetworkError {

        }
    }

//    LazyColumn(
//        modifier = Modifier.fillMaxWidth(),
//        verticalArrangement = Arrangement.spacedBy(64.dp),
//    ) {
//        items(items = uiState.falconInfo, key = { it.id }) { item ->
//            FalconInfoCard(item)
//        }
//        // Support edge-to-edge (required on Android 15)
//        // https://developer.android.com/develop/ui/compose/layouts/insets#inset-size
//        item {
//            Spacer(
//                Modifier.windowInsetsBottomHeight(
//                    WindowInsets.systemBars,
//                ),
//            )
//        }
//    }
}

@Composable
fun FalconInfoTwoRowListView(falconInfos: List<FalconInfo>, paddingValues: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = paddingValues,
        content = {
            items(falconInfos.size) { index ->
                FalconInfoCard2(falconInfos[index],
                    index,
                    onClick = {it})
            }
        })
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
                Text( "Add"
                    //stringResource(R.string.add)
               )
            }
        }
    }
}

