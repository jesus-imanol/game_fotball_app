package com.jesuscast.gamefotballapp.features.lobby.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "jugadores",
    foreignKeys = [
        ForeignKey(
            entity = RetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["retaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("retaId")]
)
data class JugadorEntity(
    @field:PrimaryKey val id: String,
    val nombre: String,
    val usuarioId: String,
    val retaId: String
)
