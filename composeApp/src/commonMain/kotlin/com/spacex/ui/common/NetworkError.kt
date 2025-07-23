package com.spacex.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.network_error_retry_button_text
import kmpspacex.composeapp.generated.resources.network_error_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NetworkError(
    modifier: Modifier = Modifier,
    message: String = stringResource(Res.string.network_error_title),
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )

//        Text(
//            text = stringResource(Res.string.network_error_description),
//            style = MaterialTheme.typography.bodySmall,
//            modifier = Modifier.padding(16.dp),
//            textAlign = TextAlign.Center,
//        )

        Button(onClick = { onRetry() }) {
            Text(
                text = stringResource(Res.string.network_error_retry_button_text).uppercase()
            )
        }
    }
}

@Preview()
@Composable
fun NetworkErrorPreview() {
    NetworkError(modifier = Modifier, onRetry = {})
}