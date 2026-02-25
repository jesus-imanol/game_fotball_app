package com.jesuscast.gamefotballapp.features.lobby.data.di

import android.content.Context
import androidx.room.Room
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.JugadorDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.RetaDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.database.LobbyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LobbyDatabaseModule {

    @Provides
    @Singleton
    fun provideLobbyDatabase(@ApplicationContext context: Context): LobbyDatabase =
        Room.databaseBuilder(
            context,
            LobbyDatabase::class.java,
            "lobby_db"
        ).build()

    @Provides
    @Singleton
    fun provideRetaDao(db: LobbyDatabase): RetaDao = db.retaDao()

    @Provides
    @Singleton
    fun provideJugadorDao(db: LobbyDatabase): JugadorDao = db.jugadorDao()
}

