package com.jesuscast.gamefotballapp.features.lobby.data.remote.websocket

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsIncomingMessage
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsRawMessageDto
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

private const val WS_URL = "ws://apigamesfotball.chuy7x.space/ws/retas"

@Singleton
class LobbyWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson
) {

    private var webSocket: WebSocket? = null

    private val _messages = MutableSharedFlow<WsIncomingMessage>(
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages: SharedFlow<WsIncomingMessage> = _messages.asSharedFlow()

    private val _connectionStatus = MutableSharedFlow<ConnectionStatus>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val connectionStatus: SharedFlow<ConnectionStatus> = _connectionStatus.asSharedFlow()

    fun connect() {
        if (webSocket != null) return
        val request = Request.Builder().url(WS_URL).build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                _connectionStatus.tryEmit(ConnectionStatus.Connected)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val raw = runCatching { gson.fromJson(text, WsRawMessageDto::class.java) }
                    .getOrNull() ?: return

                val parsed: WsIncomingMessage = when (raw.status) {
                    "nueva_reta" -> if (raw.reta != null)
                        WsIncomingMessage.NuevaReta(raw.reta)
                    else WsIncomingMessage.Unknown

                    "actualizacion" -> if (raw.retaId != null &&
                        raw.jugadoresActuales != null &&
                        raw.listaJugadores != null
                    ) WsIncomingMessage.Actualizacion(
                        retaId = raw.retaId,
                        jugadoresActuales = raw.jugadoresActuales,
                        listaJugadores = raw.listaJugadores
                    ) else WsIncomingMessage.Unknown

                    "error" -> WsIncomingMessage.Error(raw.mensaje ?: "Error desconocido")
                    else -> WsIncomingMessage.Unknown
                }
                _messages.tryEmit(parsed)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                this@LobbyWebSocketClient.webSocket = null
                _connectionStatus.tryEmit(ConnectionStatus.Disconnected(t.message))
                _messages.tryEmit(WsIncomingMessage.Error(t.message ?: "Fallo de conexión"))
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                this@LobbyWebSocketClient.webSocket = null
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
