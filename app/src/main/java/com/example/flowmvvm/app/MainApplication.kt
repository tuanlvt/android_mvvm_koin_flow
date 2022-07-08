package com.example.flowmvvm.app

import android.app.Application
import com.example.flowmvvm.di.rootModule
import com.example.flowmvvm.utils.GlideApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        sInstance = this

        startKoin {
            androidContext(this@MainApplication)
            androidFileProperties()
            modules(rootModule)
        }
    }

    override fun onLowMemory() {
        GlideApp.get(this).onLowMemory()
        super.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        GlideApp.get(this).onTrimMemory(level)
        super.onTrimMemory(level)
    }

    companion object {
        lateinit var sInstance: MainApplication
    }
}