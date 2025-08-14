package com.sortisplus.templateandroid.di

import java.time.Clock

/**
 * Simple DI module for core dependencies
 * Provides centralized access to shared services
 */
object CoreModule {
    
    /**
     * Provides a Clock instance for time operations
     */
    fun provideClock(): Clock = Clock.systemUTC()
}
