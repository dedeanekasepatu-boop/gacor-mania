package com.example

import android.app.Application
import com.example.core.common.AppContainer
import com.example.core.common.DefaultAppContainer
import com.example.core.util.GacorLogger

class GacorApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        GacorLogger.i("GacorApplication initialized - Foundation V1.0")
        container = DefaultAppContainer(this)
    }
}
