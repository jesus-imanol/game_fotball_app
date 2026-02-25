package com.jesuscast.gamefotballapp.features.lobby.data.remote.dto

import com.google.gson.annotations.SerializedName

data class JugadorDto(
    @SerializedName("id") val id: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("reta_id") val retaId: String
)

