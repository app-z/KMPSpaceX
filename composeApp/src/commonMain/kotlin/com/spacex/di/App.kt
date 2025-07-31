package com.spacex.di

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.spacex.BuildKonfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import ru.sulgik.mapkit.MapKit

class App : Application(), KoinComponent  {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@App)
        }

        initMapKit()

    }
}

fun initMapKit() {
    val apiKey = BuildKonfig.MAPKIT_KEY
    MapKit.setApiKey(apiKey)
}
