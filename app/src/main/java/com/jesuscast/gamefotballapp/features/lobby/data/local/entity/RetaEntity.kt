package com.jesuscast.gamefotballapp.features.lobby.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "retas")
data class RetaEntity(
    @field:PrimaryKey val id: String,
    val titulo: String,
    val fechaHora: String,
    val maxJugadores: Int,
    val jugadoresActuales: Int,
    val zonaId: String
)
