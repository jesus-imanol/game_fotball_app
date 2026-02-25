package com.jesuscast.gamefotballapp.features.lobby.data.di

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.JugadorDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.RetaDao
import com.jesuscast.gamefotballapp.features.lobby.data.remote.websocket.LobbyWebSocketClient
import com.jesuscast.gamefotballapp.features.lobby.data.repository.LobbyRepositoryImpl
import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LobbyRepositoryModule {

    @Provides
    @Singleton
    fun provideLobbyRepository(
        wsClient: LobbyWebSocketClient,
        retaDao: RetaDao,
        jugadorDao: JugadorDao,
        gson: Gson
    ): LobbyRepository = LobbyRepositoryImpl(wsClient, retaDao, jugadorDao, gson)
}
