package com.jesuscast.gamefotballapp.features.games.di

import com.jesuscast.gamefotballapp.core.di.GamesFotballRetrofit
import com.jesuscast.gamefotballapp.features.games.data.datasources.remote.api.GameFotballApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GameFotballNetworkModule {
    @Provides
    @Singleton
    fun provideGameFotballApi(@GamesFotballRetrofit retrofit: Retrofit): GameFotballApi {
        return retrofit.create(GameFotballApi::class.java)
    }
}