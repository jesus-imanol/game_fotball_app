package com.jesuscast.gamefotballapp.features.chat.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Incoming WebSocket message from server → client (chat) */
sealed class WsChatIncomingMessage {

    /** Initial load: all messages for the reta sent right after connecting */
    data class HistorialChat(
        val messages: List<ChatMessageDto>
    ) : WsChatIncomingMessage()

    /** A new message was sent by someone */
    data class NuevoMensaje(
        val message: ChatMessageDto
    ) : WsChatIncomingMessage()

    data class Error(
        val mensaje: String
    ) : WsChatIncomingMessage()

    object Unknown : WsChatIncomingMessage()
}

/** Raw Gson-deserialized DTO before discriminating on `status` */
data class WsChatRawMessageDto(
    @SerializedName("status") val status: String? = null,
    // historial_chat
    @SerializedName("messages") val messages: List<ChatMessageDto>? = null,
    // nuevo_mensaje
    @SerializedName("message") val message: ChatMessageDto? = null,
    // error
    @SerializedName("mensaje") val mensaje: String? = null
)

/** Outgoing action: send a chat message */
data class WsEnviarMensajeRequest(
    @SerializedName("accion") val accion: String = "enviar_mensaje",
    @SerializedName("reta_id") val retaId: String,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("mensaje") val mensaje: String
)

