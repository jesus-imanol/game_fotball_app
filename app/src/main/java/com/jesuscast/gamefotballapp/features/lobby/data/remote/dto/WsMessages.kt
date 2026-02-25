package com.jesuscast.gamefotballapp.features.lobby.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Incoming WebSocket message from server → client */
sealed class WsIncomingMessage {

    data class NuevaReta(
        val reta: RetaDto
    ) : WsIncomingMessage()

    data class Actualizacion(
        val retaId: String,
        val jugadoresActuales: Int,
        val listaJugadores: List<JugadorDto>
    ) : WsIncomingMessage()

    data class Error(
        val mensaje: String
    ) : WsIncomingMessage()

    object Unknown : WsIncomingMessage()
}

/** Raw Gson-deserialized DTO before discriminating on `status` */
data class WsRawMessageDto(
    @SerializedName("status") val status: String? = null,
    @SerializedName("reta") val reta: RetaDto? = null,
    @SerializedName("reta_id") val retaId: String? = null,
    @SerializedName("jugadores_actuales") val jugadoresActuales: Int? = null,
    @SerializedName("lista_jugadores") val listaJugadores: List<JugadorDto>? = null,
    @SerializedName("mensaje") val mensaje: String? = null
)

/** Outgoing actions from client → server */
data class WsCrearRetaRequest(
    @SerializedName("accion") val accion: String = "crear",
    @SerializedName("zona_id") val zonaId: String,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("fecha_hora") val fechaHora: String,
    @SerializedName("max_jugadores") val maxJugadores: Int,
    @SerializedName("creador_id") val creadorId: String,
    @SerializedName("creador_nombre") val creadorNombre: String
)

data class WsUnirseRetaRequest(
    @SerializedName("accion") val accion: String = "unirse",
    @SerializedName("zona_id") val zonaId: String,
    @SerializedName("reta_id") val retaId: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("nombre") val nombre: String
)

