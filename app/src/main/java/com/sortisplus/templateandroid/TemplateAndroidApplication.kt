package com.sortisplus.templateandroid

import android.app.Application
// DataStore initialization moved to shared module
import com.sortisplus.shared.SharedSDK
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class TemplateAndroidApplication : Application() {

    companion object {
        lateinit var instance: TemplateAndroidApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize Koin with Android context
        SharedSDK.initializeSDK(
            additionalModules = listOf(
                module {
                    single<android.content.Context> { this@TemplateAndroidApplication }
                }
            )
        )
    }
}
