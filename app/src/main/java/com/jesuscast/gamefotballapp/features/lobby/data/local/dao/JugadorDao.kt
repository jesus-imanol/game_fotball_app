package com.jesuscast.gamefotballapp.features.lobby.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {

    @Query("SELECT * FROM jugadores WHERE retaId = :retaId")
    fun observeByReta(retaId: String): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM jugadores WHERE retaId = :retaId")
    suspend fun getByReta(retaId: String): List<JugadorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(jugadores: List<JugadorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(jugador: JugadorEntity)

    @Query("DELETE FROM jugadores WHERE retaId = :retaId")
    suspend fun deleteByReta(retaId: String)
}

