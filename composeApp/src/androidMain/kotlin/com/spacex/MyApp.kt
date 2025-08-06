package com.spacex

import android.app.Application
import com.spacex.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import ru.sulgik.mapkit.MapKit

class MyApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MyApp)
        }

        initMapKit()

    }
}

fun initMapKit() {
    val apiKey = BuildKonfig.MAPKIT_KEY
    MapKit.setApiKey(apiKey)
}
