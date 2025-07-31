package com.spacex.ui

import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize.Companion.contentSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.ic_box_offline
import kmpspacex.composeapp.generated.resources.ic_browse
import kmpspacex.composeapp.generated.resources.ic_dollar_pin
import ru.sulgik.mapkit.PointF
import ru.sulgik.mapkit.compose.Placemark
import ru.sulgik.mapkit.compose.YandexMap
import ru.sulgik.mapkit.compose.bindToLifecycleOwner
import ru.sulgik.mapkit.compose.imageProvider
import ru.sulgik.mapkit.compose.rememberAndInitializeMapKit
import ru.sulgik.mapkit.compose.rememberCameraPositionState
import ru.sulgik.mapkit.compose.rememberPlacemarkState
import ru.sulgik.mapkit.geometry.Point
import ru.sulgik.mapkit.map.CameraPosition
import ru.sulgik.mapkit.map.IconStyle


@Composable
fun MapScreen() {

    rememberAndInitializeMapKit().bindToLifecycleOwner() // its important!

    val placemarkGeometry = Point(59.9342802, 30.3350986)


    val startPosition = CameraPosition(
        Point(59.9342802, 30.3350986),
        12.0f,
        0.0f,
        0.0f
    )

    val cameraPositionState = rememberCameraPositionState { position = startPosition }
    YandexMap(
        cameraPositionState = cameraPositionState,
        modifier = Modifier.fillMaxSize(),
    ) {
        val imageProvider = imageProvider(Res.drawable.ic_dollar_pin) // Using compose multiplatform resources
        Placemark(
            state = rememberPlacemarkState(placemarkGeometry),
            icon = imageProvider,
//            contentSize = DpSize(48.dp, 48.dp),
//            IconStyle().apply { PointF(0.5f, 1.0f) })
        )
    }
}
