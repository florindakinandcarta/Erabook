package com.example.erabook

import android.app.Application
import com.rommansabbir.networkx.NetworkXLifecycle
import com.rommansabbir.networkx.NetworkXProvider
import com.rommansabbir.networkx.SmartConfig
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration

@HiltAndroidApp
class ErabookApp:Application() {

    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = "Florinda"
        NetworkXProvider.enable(SmartConfig(this, true, NetworkXLifecycle.Application))
    }
}