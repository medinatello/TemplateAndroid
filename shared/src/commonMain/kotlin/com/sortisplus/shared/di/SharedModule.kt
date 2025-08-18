package com.sortisplus.shared.di

import com.sortisplus.shared.data.repository.AuthRepositoryImpl
import com.sortisplus.shared.data.repository.ElementRepositoryImpl
import com.sortisplus.shared.data.repository.PersonRepositoryImpl
import com.sortisplus.shared.data.repository.SettingsRepositoryImpl
import com.sortisplus.shared.database.AppDatabase
import com.sortisplus.shared.domain.repository.AuthRepository
import com.sortisplus.shared.domain.repository.ElementRepository
import com.sortisplus.shared.domain.repository.PersonRepository
import com.sortisplus.shared.domain.repository.SettingsRepository
import com.sortisplus.shared.domain.usecase.GetAppInfoUseCase
import com.sortisplus.shared.domain.usecase.auth.LoginUseCase
import com.sortisplus.shared.domain.usecase.auth.LogoutUseCase
import com.sortisplus.shared.domain.usecase.auth.ObserveAuthStateUseCase
import com.sortisplus.shared.domain.usecase.person.CreatePersonUseCase
import com.sortisplus.shared.domain.usecase.person.GetAllPersonsUseCase
import com.sortisplus.shared.domain.usecase.settings.ObserveAppSettingsUseCase
import com.sortisplus.shared.domain.usecase.settings.UpdateDarkThemeUseCase
import com.sortisplus.shared.platform.*
import com.sortisplus.shared.presentation.viewmodel.AuthenticationViewModel
import com.sortisplus.shared.presentation.viewmodel.CreatePersonViewModel
import com.sortisplus.shared.presentation.viewmodel.PersonListViewModel
import com.sortisplus.shared.presentation.viewmodel.SettingsViewModel
import io.ktor.client.*
import org.koin.dsl.module

val domainModule = module {
    // Use Cases
    single<GetAppInfoUseCase> { GetAppInfoUseCase(get(), get()) }
    single<CreatePersonUseCase> { CreatePersonUseCase(get(), get()) }
    single<GetAllPersonsUseCase> { GetAllPersonsUseCase(get()) }
    single<UpdateDarkThemeUseCase> { UpdateDarkThemeUseCase(get()) }
    single<ObserveAppSettingsUseCase> { ObserveAppSettingsUseCase(get()) }
    
    // Auth Use Cases
    single<LoginUseCase> { LoginUseCase(get()) }
    single<LogoutUseCase> { LogoutUseCase(get()) }
    single<ObserveAuthStateUseCase> { ObserveAuthStateUseCase(get()) }
}

val dataModule = module {
    single<HttpClient> { createHttpClient() }
    single<AppDatabase> { AppDatabase(get<DbDriverFactory>().createDriver()) }
    
    // Repository implementations
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<PersonRepository> { PersonRepositoryImpl(get()) }
    single<ElementRepository> { ElementRepositoryImpl(get(), get()) }
}

val presentationModule = module {
    // ViewModels
    factory { AuthenticationViewModel(get(), get(), get()) }
    factory { SettingsViewModel(get(), get()) }
    factory { PersonListViewModel(get()) }
    factory { CreatePersonViewModel(get()) }
}

val allSharedModules = listOf(
    domainModule,
    dataModule,
    presentationModule,
    platformModule
)