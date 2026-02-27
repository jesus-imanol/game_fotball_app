package com.jesuscast.gamefotballapp.features.chat.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Incoming WebSocket message from server → client (chat) */
sealed class WsChatIncomingMessage {

    /** Initial load: all messages for the reta sent right after connecting */
    data class HistorialChat(
        val mensajes: List<ChatMessageDto>
    ) : WsChatIncomingMessage()

    /** A new message was sent by someone */
    data class NuevoMensaje(
        val mensajeChat: ChatMessageDto
    ) : WsChatIncomingMessage()

    data class Error(
        val mensaje: String
    ) : WsChatIncomingMessage()

    object Unknown : WsChatIncomingMessage()
}

/** Raw Gson-deserialized DTO before discriminating on `status` */
data class WsChatRawMessageDto(
    @SerializedName("status") val status: String? = null,
    // historial_chat → "mensajes"
    @SerializedName("mensajes") val mensajes: List<ChatMessageDto>? = null,
    // nuevo_mensaje → "mensaje_chat"
    @SerializedName("mensaje_chat") val mensajeChat: ChatMessageDto? = null,
    // error → "mensaje"
    @SerializedName("mensaje") val mensaje: String? = null
)

/** Outgoing action: send a chat message (only usuario_id + texto needed after joining) */
data class WsEnviarMensajeRequest(
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("texto") val texto: String
)

