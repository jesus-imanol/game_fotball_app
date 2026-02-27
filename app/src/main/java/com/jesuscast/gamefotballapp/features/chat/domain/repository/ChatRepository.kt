package com.jesuscast.gamefotballapp.features.chat.domain.repository

import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    /** Observes chat messages for a reta from Room (SSOT). */
    fun observeMessages(retaId: String): Flow<List<ChatMessage>>

    /** Connects the chat WebSocket for the given reta. */
    fun conectar(retaId: String)

    /** Disconnects the chat WebSocket. */
    fun desconectar()

    /** Sends a chat message through the WebSocket. */
    suspend fun enviarMensaje(retaId: String, usuarioId: String, nombre: String, mensaje: String)
}

