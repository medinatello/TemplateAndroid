package com.sortisplus.shared.di

import com.sortisplus.shared.platform.*
import org.koin.dsl.module

actual val platformModule = module {
    single<DbDriverFactory> { DbDriverFactory() }
    single<KeyValueStore> { DesktopKeyValueStore() }
    single<AppClock> { DesktopAppClock() }
}