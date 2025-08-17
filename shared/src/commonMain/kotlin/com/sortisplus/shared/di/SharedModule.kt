package com.sortisplus.shared.di

import com.sortisplus.shared.database.AppDatabase
import com.sortisplus.shared.domain.usecase.GetAppInfoUseCase
import com.sortisplus.shared.platform.*
import io.ktor.client.*
import org.koin.dsl.module

val sharedModule = module {
    single<HttpClient> { createHttpClient() }
    single<AppDatabase> { AppDatabase(get<DbDriverFactory>().createDriver()) }
    single<GetAppInfoUseCase> { GetAppInfoUseCase(get(), get()) }
}

val allSharedModules = listOf(
    sharedModule,
    platformModule
)