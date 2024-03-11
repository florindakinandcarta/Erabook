package com.example.erabook

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ErabookApp:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}