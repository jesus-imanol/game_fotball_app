package com.jesuscast.gamefotballapp.features.lobby.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.JugadorDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.RetaDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.JugadorEntity
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.RetaEntity

@Database(
    entities = [RetaEntity::class, JugadorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LobbyDatabase : RoomDatabase() {
    abstract fun retaDao(): RetaDao
    abstract fun jugadorDao(): JugadorDao
}

