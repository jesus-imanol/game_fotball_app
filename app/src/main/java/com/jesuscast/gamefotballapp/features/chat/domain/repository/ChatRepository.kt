package com.jesuscast.gamefotballapp.features.chat.domain.repository

import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /** Observes chat messages for a reta from Room (SSOT). */
    fun observeMessages(retaId: String): Flow<List<ChatMessage>>

    /** Connects the chat WebSocket for the given reta and zona. */
    fun conectar(retaId: String, zonaId: String)

    /** Disconnects the chat WebSocket. */
    fun desconectar()

    /** Sends a chat message through the WebSocket (only usuario_id + texto needed). */
    suspend fun enviarMensaje(usuarioId: String, texto: String)
}

