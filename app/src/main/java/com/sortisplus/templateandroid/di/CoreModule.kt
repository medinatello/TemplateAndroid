package com.sortisplus.templateandroid.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

/**
 * Hilt module for core dependencies
 * Provides centralized access to shared services
 */
@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    
    /**
     * Provides a Clock instance for time operations
     */
    @Provides
    @Singleton
    fun provideClock(): Clock = Clock.systemUTC()
}
