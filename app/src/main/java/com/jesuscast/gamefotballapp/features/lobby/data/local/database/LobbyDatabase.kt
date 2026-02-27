package com.jesuscast.gamefotballapp.features.lobby.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.JugadorDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.RetaDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.JugadorEntity
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.RetaEntity

@Database(
    entities = [RetaEntity::class, JugadorEntity::class],
    version = 2,
    exportSchema = false
)
abstract class LobbyDatabase : RoomDatabase() {
    abstract fun retaDao(): RetaDao
    abstract fun jugadorDao(): JugadorDao
}

/** Adds creadorId and creadorNombre columns (both with empty-string default). */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE retas ADD COLUMN creadorId TEXT NOT NULL DEFAULT ''")
        db.execSQL("ALTER TABLE retas ADD COLUMN creadorNombre TEXT NOT NULL DEFAULT ''")
    }
}

