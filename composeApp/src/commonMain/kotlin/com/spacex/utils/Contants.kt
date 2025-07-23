package com.spacex.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import kmpspacex.composeapp.generated.resources.Res
import kmpspacex.composeapp.generated.resources.dark_mode
import kmpspacex.composeapp.generated.resources.light_mode
import kmpspacex.composeapp.generated.resources.system_default
import org.jetbrains.compose.resources.StringResource

const val BASE_URL = "https://api.spacexdata.com/v4/launches"

const val DB_Name = "spacex.db"

const val dataStoreFileName = "setting.preferences_pb"

val FadeIn = fadeIn(animationSpec = tween(220, delayMillis = 90)) +
        scaleIn(
            initialScale = 0.92f,
            animationSpec = tween(220, delayMillis = 90)
        )

val FadeOut = fadeOut(animationSpec = tween(90))

const val ERROR_LOADING_DATA = 1

enum class Theme(val title: StringResource) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode)
}
