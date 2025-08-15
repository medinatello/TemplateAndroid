package com.sortisplus.templateandroid

import android.app.Application
import com.sortisplus.core.datastore.DataStoreInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TemplateAndroidApplication : Application() {

    @Inject
    lateinit var dataStoreInitializer: DataStoreInitializer

    companion object {
        lateinit var instance: TemplateAndroidApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize DataStore and perform migrations
        dataStoreInitializer.initialize()
    }
}
