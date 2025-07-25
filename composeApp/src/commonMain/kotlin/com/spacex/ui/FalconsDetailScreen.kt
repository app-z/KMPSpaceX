package com.spacex.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.spacex.model.FalconInfo
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FalconsDetailScreen(
    rootNavController: NavHostController,
    falconInfo: FalconInfo,
    paddingValues: PaddingValues
) {
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
                IconButton(onClick = { rootNavController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                modifier = Modifier.fillMaxWidth()
            )

            AsyncImage(
                placeholder = rememberVectorPainter(Icons.Filled.Rocket),
                model = falconInfo.pathLarge,
                //imageVector = Icons.Filled.Rocket,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.padding(12.dp)
                    .fillMaxWidth()
                    .clip(CircleShape)
                //.aspectRatio(0.8F)
            )

            Text(
                text = falconInfo.description ?: "No description",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
@Preview
fun PreviewFalconsDetailScreen() {

    val rootNavController = rememberNavController()

    FalconsDetailScreen(
        rootNavController = rootNavController,
        falconInfo = FalconInfo(
            id = "",
            name = "sadasdasdadsad",
            dateUtc = "10.10.2020",
            links = null,
            pathSmall = "https://www.kasandbox.org/programming-images/avatars/leaf-blue.png",
            pathLarge = null,
            description = "asdadsad asdasdad asdasdsad wtewtet sdfsd sdsdfsdf  werwerew"
        ),
        paddingValues = PaddingValues(16.dp)
    )

}
