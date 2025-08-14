package com.sortisplus.templateandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TemplateAndroidApplication : Application() {

    companion object {
        lateinit var instance: TemplateAndroidApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
