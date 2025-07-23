package com.spacex.di

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class App : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(Factory(this))


    }
}

class My : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val pi = goAsync()
    }

}