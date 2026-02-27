package com.jesuscast.gamefotballapp.features.chat.data.repository

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.chat.data.local.dao.ChatMessageDao
import com.jesuscast.gamefotballapp.features.chat.data.mapper.toDomain
import com.jesuscast.gamefotballapp.features.chat.data.mapper.toEntity
import com.jesuscast.gamefotballapp.features.chat.data.remote.dto.WsChatIncomingMessage
import com.jesuscast.gamefotballapp.features.chat.data.remote.dto.WsEnviarMensajeRequest
import com.jesuscast.gamefotballapp.features.chat.data.remote.websocket.ChatWebSocketClient
import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage
import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatRepositoryImpl(
    private val wsClient: ChatWebSocketClient,
    private val chatMessageDao: ChatMessageDao,
    private val gson: Gson
) : ChatRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentRetaId: String = ""

    // ─── SSOT: WebSocket → Room ──────────────────────────────────────────────
    init {
        scope.launch {
            wsClient.messages.collect { message ->
                when (message) {
                    is WsChatIncomingMessage.HistorialChat -> {
                        // Initial load: replace all messages for this reta in Room
                        chatMessageDao.deleteByReta(currentRetaId)
                        chatMessageDao.upsertAll(message.messages.map { it.toEntity() })
                    }
                    is WsChatIncomingMessage.NuevoMensaje -> {
                        chatMessageDao.upsert(message.message.toEntity())
                    }
                    // Errors handled at ViewModel level via wsClient.messages collector
                    else -> Unit
                }
            }
        }
    }

    // ─── ChatRepository ─────────────────────────────────────────────────────

    override fun observeMessages(retaId: String): Flow<List<ChatMessage>> =
        chatMessageDao.observeByReta(retaId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun conectar(retaId: String) {
        currentRetaId = retaId
        wsClient.disconnect()
        wsClient.connect(retaId)
    }

    override fun desconectar() {
        wsClient.disconnect()
    }

    override suspend fun enviarMensaje(
        retaId: String,
        usuarioId: String,
        nombre: String,
        mensaje: String
    ) {
        val request = WsEnviarMensajeRequest(
            retaId = retaId,
            usuarioId = usuarioId,
            nombre = nombre,
            mensaje = mensaje
        )
        val json = gson.toJson(request)
        val sent = wsClient.send(json)
        if (!sent) {
            throw Exception("No se pudo enviar el mensaje. El WebSocket no está conectado.")
        }
    }
}

