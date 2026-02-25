package com.jesuscast.gamefotballapp.features.lobby.data.di

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.lobby.data.remote.websocket.LobbyWebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LobbyNetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideLobbyOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .pingInterval(20, TimeUnit.SECONDS)   // keep WebSocket alive
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)     // no read timeout for WebSocket
            .build()
    }

    @Provides
    @Singleton
    fun provideLobbyWebSocketClient(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): LobbyWebSocketClient = LobbyWebSocketClient(okHttpClient, gson)
}

