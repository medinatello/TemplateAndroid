package com.sortisplus.shared

import com.sortisplus.shared.di.allSharedModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

object SharedSDK {
    
    fun initializeSDK(additionalModules: List<Module> = emptyList()) {
        startKoin {
            modules(allSharedModules + additionalModules)
        }
    }
    
    fun tearDown() {
        stopKoin()
    }
}