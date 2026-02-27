package com.jesuscast.gamefotballapp.features.chat.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jesuscast.gamefotballapp.features.chat.data.local.dao.ChatMessageDao
import com.jesuscast.gamefotballapp.features.chat.data.local.entity.ChatMessageEntity

@Database(
    entities = [ChatMessageEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}

