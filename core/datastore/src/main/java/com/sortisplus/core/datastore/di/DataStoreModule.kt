package com.sortisplus.core.datastore.di

import android.content.Context
import com.sortisplus.core.datastore.AuthenticationManager
import com.sortisplus.core.datastore.ConfigurationManager
import com.sortisplus.core.datastore.DataStoreInitializer
import com.sortisplus.core.datastore.PreferencesRepository
import com.sortisplus.core.datastore.SecureStorage
import com.sortisplus.core.datastore.SettingsDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de inyección de dependencias para el sistema de almacenamiento y configuración
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(
        @ApplicationContext context: Context
    ): SettingsDataStore {
        return SettingsDataStore(context)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(
        settingsDataStore: SettingsDataStore
    ): PreferencesRepository {
        return PreferencesRepository(settingsDataStore)
    }

    @Provides
    @Singleton
    fun provideSecureStorage(
        @ApplicationContext context: Context
    ): SecureStorage {
        return SecureStorage(context)
    }

    @Provides
    @Singleton
    fun provideConfigurationManager(
        preferencesRepository: PreferencesRepository,
        secureStorage: SecureStorage
    ): ConfigurationManager {
        return ConfigurationManager(preferencesRepository, secureStorage)
    }

    @Provides
    @Singleton
    fun provideDataStoreInitializer(
        @ApplicationContext context: Context,
        configurationManager: ConfigurationManager
    ): DataStoreInitializer {
        return DataStoreInitializer(context, configurationManager)
    }

    @Provides
    @Singleton
    fun provideAuthenticationManager(
        configurationManager: ConfigurationManager
    ): AuthenticationManager {
        return AuthenticationManager(configurationManager)
    }
}
