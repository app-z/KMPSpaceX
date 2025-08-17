package com.spacex.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import com.spacex.model.FalconInfo
import com.spacex.viewmodel.FalconDetailViewModel
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_bookmark_filled
import kmpspacex.composeapp.generated.resources.ic_bookmark_outlined
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FalconsDetailScreen(
    rootNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    falconInfo: FalconInfo,
    paddingValues: PaddingValues
) {

    val viewModel =
        koinViewModel<FalconDetailViewModel>(
            parameters = {
                parametersOf(falconInfo.id)
            }
        )

    FalconsDetailScreenContent(
        rootNavController = rootNavController,
        falconInfo = falconInfo,
        isBookmarked = viewModel.isBookmarked,
        paddingValues = paddingValues,
        onBookMark = {
            viewModel.bookmarkArticle(falconInfo)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalconsDetailScreenContent(
    rootNavController: NavHostController,
    falconInfo: FalconInfo,
    isBookmarked: Boolean,
    paddingValues: PaddingValues,
    onBookMark: (falconInfo: FalconInfo) -> Unit
) {

    val scope = rememberCoroutineScope()

    var showProgress by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Falcon Detail",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    rootNavController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    onBookMark.invoke(falconInfo)
                }) {
                    Icon(
                        painter = painterResource(
                            if (isBookmarked) Res.drawable.ic_bookmark_filled
                            else Res.drawable.ic_bookmark_outlined
                        ),
                        contentDescription = null,
                    )
                }
            }
        )

        Column(Modifier.padding(paddingValues)) {

            Text(
                text = falconInfo.name,
                style = MaterialTheme.typography.headlineMedium + TextStyle(textAlign = TextAlign.Center),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )

            AsyncImage(
                model = falconInfo.pathLarge,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.padding(12.dp)
                    .fillMaxWidth(),
                onState = { state ->
                    showProgress = when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            true
                        }

                        is AsyncImagePainter.State.Error -> {
                            // Handle error, e.g., show a placeholder or error icon
                            false
                        }

                        else -> {
                            false
                        }
                    }
                }

            )

            if (showProgress) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .fillMaxSize()
                    )
                }
            }

            Text(
                text = falconInfo.details ?: "No description",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

    }
}


@Composable
@Preview
fun PreviewFalconsDetailScreen1() {

    val rootNavController = rememberNavController()

    FalconsDetailScreenContent(
        rootNavController = rootNavController,
        isBookmarked = true,
        falconInfo = FalconInfo(
            id = "",
            name = "sadasdasdadsad",
            dateUtc = "10.10.2020",
            links = null,
            pathSmall = "https://www.kasandbox.org/programming-images/avatars/leaf-blue.png",
            pathLarge = null,
            description = "asdadsad asdasdad asdasdsad wtewtet sdfsd sdsdfsdf  werwerew"
        ),
        paddingValues = PaddingValues(16.dp),
        onBookMark = {}
    )
}

@Composable
@Preview
fun PreviewFalconsDetailScreen2() {

    val rootNavController = rememberNavController()

    FalconsDetailScreenContent(
        rootNavController = rootNavController,
        isBookmarked = false,
        falconInfo = FalconInfo(
            id = "",
            name = "sadasdasdadsad",
            dateUtc = "10.10.2020",
            links = null,
            pathSmall = "https://www.kasandbox.org/programming-images/avatars/leaf-blue.png",
            pathLarge = null,
            description = "asdadsad asdasdad asdasdsad wtewtet sdfsd sdsdfsdf  werwerew"
        ),
        paddingValues = PaddingValues(16.dp),
        onBookMark = {}
    )
}
