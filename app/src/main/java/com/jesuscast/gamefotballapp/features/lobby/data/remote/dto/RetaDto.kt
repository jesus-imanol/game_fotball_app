package com.jesuscast.gamefotballapp.features.lobby.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RetaDto(
    @SerializedName("id") val id: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("fecha_hora") val fechaHora: String,
    @SerializedName("max_jugadores") val maxJugadores: Int,
    @SerializedName("jugadores_actuales") val jugadoresActuales: Int,
    @SerializedName("lista_jugadores") val listaJugadores: List<JugadorDto>,
    @SerializedName("creador_id") val creadorId: String? = null,
    @SerializedName("creador_nombre") val creadorNombre: String? = null
)

