package com.sortisplus.templateandroid

import android.app.Application
import java.time.Clock

class TemplateAndroidApplication : Application() {

    companion object {
        lateinit var instance: TemplateAndroidApplication
            private set
        
        // Simple DI container
        val clock: Clock = Clock.systemUTC()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
