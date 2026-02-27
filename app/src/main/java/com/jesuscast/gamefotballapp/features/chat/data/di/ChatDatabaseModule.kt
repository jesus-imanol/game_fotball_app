package com.jesuscast.gamefotballapp.features.chat.data.di

import android.content.Context
import androidx.room.Room
import com.jesuscast.gamefotballapp.features.chat.data.local.dao.ChatMessageDao
import com.jesuscast.gamefotballapp.features.chat.data.local.database.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatDatabaseModule {

    @Provides
    @Singleton
    fun provideChatDatabase(@ApplicationContext context: Context): ChatDatabase =
        Room.databaseBuilder(
            context,
            ChatDatabase::class.java,
            "chat_db"
        ).build()

    @Provides
    @Singleton
    fun provideChatMessageDao(db: ChatDatabase): ChatMessageDao = db.chatMessageDao()
}

