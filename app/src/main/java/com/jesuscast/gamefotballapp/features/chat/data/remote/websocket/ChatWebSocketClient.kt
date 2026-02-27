package com.jesuscast.gamefotballapp.features.chat.data.remote.websocket

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.chat.data.remote.dto.WsChatIncomingMessage
import com.jesuscast.gamefotballapp.features.chat.data.remote.dto.WsChatRawMessageDto
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

private const val WS_URL = "wss://apigamesfotball.chuy7x.space/ws/retas/chat"

@Singleton
class ChatWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {

    private var webSocket: WebSocket? = null

    private val _messages = MutableSharedFlow<WsChatIncomingMessage>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages: SharedFlow<WsChatIncomingMessage> = _messages.asSharedFlow()

    private val _connectionStatus = MutableSharedFlow<ConnectionStatus>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionStatus: SharedFlow<ConnectionStatus> = _connectionStatus.asSharedFlow()

    private var pendingRetaId: String = ""
    private var pendingZonaId: String = ""

    /**
     * Connects the WebSocket and on open immediately sends
     * reta_id + zona_id so the server responds with historial_chat.
     */
    fun connect(retaId: String, zonaId: String) {
        pendingRetaId = retaId
        pendingZonaId = zonaId
        if (webSocket != null) return
        val request = Request.Builder().url(WS_URL).build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionStatus.tryEmit(ConnectionStatus.Connected)
                if (pendingRetaId.isNotBlank() && pendingZonaId.isNotBlank()) {
                    val subscribe = gson.toJson(
                        mapOf("reta_id" to pendingRetaId, "zona_id" to pendingZonaId)
                    )
                    webSocket.send(subscribe)
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val raw = runCatching { gson.fromJson(text, WsChatRawMessageDto::class.java) }
                    .getOrNull() ?: return

                val parsed: WsChatIncomingMessage = when (raw.status) {
                    "historial_chat" -> WsChatIncomingMessage.HistorialChat(
                        raw.mensajes ?: emptyList()
                    )

                    "nuevo_mensaje" -> if (raw.mensajeChat != null)
                        WsChatIncomingMessage.NuevoMensaje(raw.mensajeChat)
                    else WsChatIncomingMessage.Unknown

                    "error" -> WsChatIncomingMessage.Error(
                        raw.mensaje ?: "Error desconocido"
                    )

                    else -> WsChatIncomingMessage.Unknown
                }
                _messages.tryEmit(parsed)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                this@ChatWebSocketClient.webSocket = null
                _connectionStatus.tryEmit(ConnectionStatus.Disconnected(t.message))
                _messages.tryEmit(
                    WsChatIncomingMessage.Error(t.message ?: "Fallo de conexión")
                )
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@ChatWebSocketClient.webSocket = null
                _connectionStatus.tryEmit(ConnectionStatus.Disconnected(reason))
            }
        })
    }

    fun send(json: String): Boolean = webSocket?.send(json) ?: false

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
    }

    sealed class ConnectionStatus {
        object Connected : ConnectionStatus()
        data class Disconnected(val reason: String?) : ConnectionStatus()
    }
}

