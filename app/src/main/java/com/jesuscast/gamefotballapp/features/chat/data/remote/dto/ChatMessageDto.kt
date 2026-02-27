package com.jesuscast.gamefotballapp.features.chat.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatMessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("reta_id") val retaId: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("timestamp") val timestamp: Long
)

