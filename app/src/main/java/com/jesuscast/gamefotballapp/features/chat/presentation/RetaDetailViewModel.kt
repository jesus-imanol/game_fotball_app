package com.jesuscast.gamefotballapp.features.chat.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesuscast.gamefotballapp.core.session.SessionManager
import com.jesuscast.gamefotballapp.features.chat.data.remote.dto.WsChatIncomingMessage
import com.jesuscast.gamefotballapp.features.chat.data.remote.websocket.ChatWebSocketClient
import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository
import com.jesuscast.gamefotballapp.features.chat.domain.usecase.EnviarMensajeUseCase
import com.jesuscast.gamefotballapp.features.chat.domain.usecase.ObservarMensajesUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.ObservarRetasUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.UnirseARetaUseCase
import com.jesuscast.gamefotballapp.features.lobby.presentation.WsConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RetaDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val chatRepository: ChatRepository,
    private val chatWsClient: ChatWebSocketClient,
    private val observarMensajesUseCase: ObservarMensajesUseCase,
    private val enviarMensajeUseCase: EnviarMensajeUseCase,
    private val observarRetasUseCase: ObservarRetasUseCase,
    private val unirseARetaUseCase: UnirseARetaUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val retaId: String = savedStateHandle["retaId"] ?: ""
    private val zonaId: String = savedStateHandle["zonaId"] ?: ""

    private val _uiState = MutableStateFlow(RetaDetailState())
    val uiState: StateFlow<RetaDetailState> = _uiState.asStateFlow()

    init {
        loadSessionData()
        listenConnectionStatus()
        conectarChat()
        observeReta()
        observeMessages()
        listenForChatWsMessages()
    }

    // ─── Session ──────────────────────────────────────────────────────────────

    private fun loadSessionData() {
        viewModelScope.launch {
            val userId = sessionManager.getUserIdOnce() ?: ""
            val nombre = sessionManager.getNombreOnce() ?: ""
            _uiState.update { it.copy(currentUserId = userId, currentUserNombre = nombre) }
        }
    }

    // ─── WS connection status ─────────────────────────────────────────────────

    private fun listenConnectionStatus() {
        viewModelScope.launch {
            chatWsClient.connectionStatus.collect { status ->
                when (status) {
                    is ChatWebSocketClient.ConnectionStatus.Connected -> {
                        _uiState.update { it.copy(wsConnectionState = WsConnectionState.CONNECTED) }
                    }
                    is ChatWebSocketClient.ConnectionStatus.Disconnected -> {
                        _uiState.update { it.copy(wsConnectionState = WsConnectionState.DISCONNECTED) }
                    }
                }
            }
        }
    }

    // ─── Connect chat ────────────────────────────────────────────────────────

    private fun conectarChat() {
        _uiState.update { it.copy(wsConnectionState = WsConnectionState.CONNECTING) }
        chatRepository.conectar(retaId)
    }

    // ─── Observe reta from lobby Room DB ─────────────────────────────────────

    private fun observeReta() {
        viewModelScope.launch {
            observarRetasUseCase(zonaId)
                .catch { /* ignore */ }
                .collect { retas ->
                    val reta = retas.find { it.id == retaId }
                    _uiState.update { it.copy(reta = reta, isLoading = false) }
                }
        }
    }

    // ─── Observe chat messages from Room (SSOT) ──────────────────────────────

    private fun observeMessages() {
        viewModelScope.launch {
            observarMensajesUseCase(retaId)
                .catch { /* ignore */ }
                .collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
                }
        }
    }

    // ─── WS message listener (errors) ────────────────────────────────────────

    private fun listenForChatWsMessages() {
        viewModelScope.launch {
            chatWsClient.messages.collect { message ->
                when (message) {
                    is WsChatIncomingMessage.Error -> {
                        // Ignore harmless initial errors
                        if (message.mensaje.contains("no reconocida", ignoreCase = true)) return@collect
                    }
                    else -> Unit
                }
            }
        }
    }

    // ─── Message input ───────────────────────────────────────────────────────

    fun onMessageChanged(text: String) {
        _uiState.update { it.copy(currentMessage = text) }
    }

    fun onSendMessage() {
        val state = _uiState.value
        val msg = state.currentMessage.trim()
        if (msg.isBlank()) return
        if (state.currentUserId.isBlank()) return
        if (state.wsConnectionState != WsConnectionState.CONNECTED) return

        _uiState.update { it.copy(currentMessage = "") }

        viewModelScope.launch {
            try {
                enviarMensajeUseCase(
                    retaId = retaId,
                    usuarioId = state.currentUserId,
                    nombre = state.currentUserNombre,
                    mensaje = msg
                )
            } catch (_: Exception) {
                // Restore message on failure
                _uiState.update { it.copy(currentMessage = msg) }
            }
        }
    }

    // ─── Join reta ───────────────────────────────────────────────────────────

    fun onUnirse() {
        val state = _uiState.value
        val reta = state.reta ?: return
        if (state.pendingJoinRetaId != null) return
        if (state.currentUserId.isBlank()) return

        val alreadyIn = reta.listaJugadores.any { it.usuarioId == state.currentUserId }
        if (alreadyIn) return

        if (state.wsConnectionState != WsConnectionState.CONNECTED) return

        _uiState.update { it.copy(pendingJoinRetaId = reta.id) }

        viewModelScope.launch {
            try {
                unirseARetaUseCase(
                    zonaId = zonaId,
                    retaId = reta.id,
                    usuarioId = state.currentUserId,
                    nombre = state.currentUserNombre
                )
                // Wait for server confirmation via lobby WS
                launch {
                    kotlinx.coroutines.delay(8_000)
                    if (_uiState.value.pendingJoinRetaId == reta.id) {
                        _uiState.update { it.copy(pendingJoinRetaId = null) }
                    }
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(pendingJoinRetaId = null) }
            }
        }
    }

    // ─── Lifecycle ───────────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        chatRepository.desconectar()
    }
}



