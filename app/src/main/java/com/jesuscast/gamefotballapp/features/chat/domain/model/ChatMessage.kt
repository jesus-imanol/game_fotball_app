package com.jesuscast.gamefotballapp.features.chat.domain.model

data class ChatMessage(
    val id: String,
    val retaId: String,
    val usuarioId: String,
    val nombreUsuario: String,
    val mensaje: String,
    val timestamp: Long
)

