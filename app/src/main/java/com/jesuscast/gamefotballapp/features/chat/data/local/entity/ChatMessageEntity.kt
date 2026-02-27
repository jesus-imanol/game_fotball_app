package com.jesuscast.gamefotballapp.features.chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val retaId: String,
    val usuarioId: String,
    val nombreUsuario: String,
    val mensaje: String,
    val timestamp: Long
)

