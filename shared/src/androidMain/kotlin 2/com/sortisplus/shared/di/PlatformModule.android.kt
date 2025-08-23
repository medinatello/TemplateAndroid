package com.sortisplus.shared.di

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.sortisplus.shared.platform.*
import org.koin.dsl.module

actual val platformModule = module {
    single<DbDriverFactory> { DbDriverFactory(get<Context>()) }
    single<KeyValueStore> { 
        AndroidKeyValueStore(
            SharedPreferencesSettings(
                get<Context>().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            )
        ) 
    }
    single<AppClock> { AndroidAppClock() }
}