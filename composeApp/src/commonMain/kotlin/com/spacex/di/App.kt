package com.spacex.di

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent

class App : Application(), KoinComponent  {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
//    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@App)
        }

  //      container = AppContainer(Factory())

    }
}


//class MyApp : Application(), KoinComponent {
//    override fun onCreate() {
//        super.onCreate()
//        initKoin {
//            androidLogger()
//            androidContext(this@MyApp)
//        }
//    }
//}

class AppBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val pi = goAsync()
    }
}
