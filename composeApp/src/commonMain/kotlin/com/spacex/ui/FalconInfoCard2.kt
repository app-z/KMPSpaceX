package com.spacex.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.spacex.model.FalconInfo
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_bookmark_filled
import kmpspacex.composeapp.generated.resources.ic_bookmark_outlined
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FalconInfoCard2(
    falconInfo: FalconInfo,
    index: Int,
    onClick: (index: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(4.dp),
        shape = RoundedCornerShape(30.dp),
    ) {

        Column (modifier = Modifier
            .wrapContentSize()
            .clickable(onClick = { onClick.invoke(index) })) {

            Box {

                Icon(
                    modifier = Modifier.padding(16.dp).align(Alignment.TopEnd),
                    painter = painterResource(
                        if (falconInfo.isBookMark) Res.drawable.ic_bookmark_filled
                        else Res.drawable.ic_bookmark_outlined
                    ),
                    contentDescription = null,
                )

                AsyncImage(
                    placeholder = rememberVectorPainter(Icons.Filled.Rocket),
                    model = falconInfo.pathSmall,
                    //imageVector = Icons.Filled.Rocket,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.padding(top = 12.dp)
                        .size(220.dp)
                        .clip(CircleShape)
                    //.aspectRatio(0.8F)
                )
            }

            Text(
                maxLines = 1,
                text = falconInfo.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(bottom = 12.dp)
            )

        }
    }
}


@Preview
@Composable
fun PreviewMessageCard2() {
    FalconInfoCard2(
        FalconInfo(
            dateUtc = "234234234",
            name = "Rocket 1",
            id = "N1",
            isBookMark = true,
            description = "Detail can be long, Detail can be long, Detail can be long, "
        ),0, onClick = {}
    )
}
