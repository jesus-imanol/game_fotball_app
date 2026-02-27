package com.jesuscast.gamefotballapp.features.chat.data.di

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.chat.data.remote.websocket.ChatWebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatNetworkModule {

    @Provides
    @Singleton
    fun provideChatWebSocketClient(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): ChatWebSocketClient = ChatWebSocketClient(okHttpClient, gson)
}

