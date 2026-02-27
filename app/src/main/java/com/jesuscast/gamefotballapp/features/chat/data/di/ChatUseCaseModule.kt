package com.jesuscast.gamefotballapp.features.chat.data.di

import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository
import com.jesuscast.gamefotballapp.features.chat.domain.usecase.EnviarMensajeUseCase
import com.jesuscast.gamefotballapp.features.chat.domain.usecase.ObservarMensajesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ChatUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideObservarMensajesUseCase(repo: ChatRepository): ObservarMensajesUseCase =
        ObservarMensajesUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideEnviarMensajeUseCase(repo: ChatRepository): EnviarMensajeUseCase =
        EnviarMensajeUseCase(repo)
}

