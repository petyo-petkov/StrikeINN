package com.example.strikeinn

import android.app.Application
import com.example.strikeinn.di.networkModule
import com.example.strikeinn.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                networkModule,
                viewModelModule
            )
        }

    }

}