package com.jesuscast.gamefotballapp.features.lobby.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.RetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RetaDao {

    @Query("SELECT * FROM retas WHERE zonaId = :zonaId ORDER BY fechaHora ASC")
    fun observeByZona(zonaId: String): Flow<List<RetaEntity>>

    @Query("SELECT * FROM retas WHERE id = :retaId")
    suspend fun getById(retaId: String): RetaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(reta: RetaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(retas: List<RetaEntity>)

    @Query(
        """UPDATE retas 
           SET jugadoresActuales = :jugadoresActuales 
           WHERE id = :retaId"""
    )
    suspend fun updateJugadoresActuales(retaId: String, jugadoresActuales: Int)

    @Query("DELETE FROM retas WHERE id = :retaId")
    suspend fun deleteById(retaId: String)

    @Query("DELETE FROM retas WHERE zonaId = :zonaId")
    suspend fun deleteByZona(zonaId: String)
}
