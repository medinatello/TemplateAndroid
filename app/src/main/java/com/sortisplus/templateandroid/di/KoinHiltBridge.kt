package com.sortisplus.templateandroid.di

import com.sortisplus.shared.domain.usecase.GetAppInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.java.KoinJavaComponent.inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KoinHiltBridge {
    
    @Provides
    @Singleton
    fun provideGetAppInfoUseCase(): GetAppInfoUseCase {
        val useCase: GetAppInfoUseCase by inject(GetAppInfoUseCase::class.java)
        return useCase
    }
}