package com.jesuscast.gamefotballapp.features.chat.data.di

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.chat.data.local.dao.ChatMessageDao
import com.jesuscast.gamefotballapp.features.chat.data.remote.websocket.ChatWebSocketClient
import com.jesuscast.gamefotballapp.features.chat.data.repository.ChatRepositoryImpl
import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatRepositoryModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        wsClient: ChatWebSocketClient,
        chatMessageDao: ChatMessageDao,
        gson: Gson
    ): ChatRepository = ChatRepositoryImpl(wsClient, chatMessageDao, gson)
}

