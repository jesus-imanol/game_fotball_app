package com.jesuscast.gamefotballapp.features.lobby.data.di

import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.CrearRetaUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.ObservarRetasUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.UnirseARetaUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LobbyUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideObservarRetasUseCase(repo: LobbyRepository): ObservarRetasUseCase =
        ObservarRetasUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideUnirseARetaUseCase(repo: LobbyRepository): UnirseARetaUseCase =
        UnirseARetaUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideCrearRetaUseCase(repo: LobbyRepository): CrearRetaUseCase =
        CrearRetaUseCase(repo)
}

