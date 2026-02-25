package com.jesuscast.gamefotballapp.features.lobby.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsIncomingMessage
import com.jesuscast.gamefotballapp.features.lobby.data.remote.websocket.LobbyWebSocketClient
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.CrearRetaUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.ObservarRetasUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.UnirseARetaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    private val repository: LobbyRepository,
    private val wsClient: LobbyWebSocketClient,
    private val observarRetasUseCase: ObservarRetasUseCase,
    private val unirseARetaUseCase: UnirseARetaUseCase,
    private val crearRetaUseCase: CrearRetaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyState())
    val uiState: StateFlow<LobbyState> = _uiState.asStateFlow()

    private val _errors = MutableSharedFlow<String>(
        extraBufferCapacity = 4,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val errors: SharedFlow<String> = _errors.asSharedFlow()

    // Placeholder user — replace with your auth module
    private val currentUserId = "user_demo"
    private val currentUserNombre = "Jugador Demo"

    init {
        conectarZona(_uiState.value.zonaSeleccionada)
        listenForWsErrors()
    }

    // ─── Zone management ─────────────────────────────────────────────────────

    fun onZonaSeleccionada(zona: String) {
        if (zona == _uiState.value.zonaSeleccionada) return
        _uiState.update { it.copy(zonaSeleccionada = zona, isLoading = true, retas = emptyList()) }
        conectarZona(zona)
    }

    private fun conectarZona(zonaId: String) {
        repository.conectar(zonaId)
        viewModelScope.launch {
            observarRetasUseCase(zonaId)
                .catch { e -> _errors.emit(e.message ?: "Error al cargar retas") }
                .collect { retas ->
                    _uiState.update { it.copy(retas = retas, isLoading = false) }
                }
        }
    }

    // ─── Optimistic join ─────────────────────────────────────────────────────

    fun onUnirse(reta: Reta) {
        if (_uiState.value.pendingJoinRetaId != null) return   // already waiting

        val snapshotJugadoresActuales = reta.jugadoresActuales
        val snapshotListaJugadoresIds = reta.listaJugadores.map { it.id }

        // 1. Optimistic update → mark as pending
        _uiState.update { it.copy(pendingJoinRetaId = reta.id) }

        viewModelScope.launch {
            try {
                // 2. Send WS message
                unirseARetaUseCase(
                    zonaId = _uiState.value.zonaSeleccionada,
                    retaId = reta.id,
                    usuarioId = currentUserId,
                    nombre = currentUserNombre
                )
                // 3. Clear pending (the server will push actualizacion which Room will persist)
                _uiState.update { it.copy(pendingJoinRetaId = null) }
            } catch (e: Exception) {
                // Rollback on local exception
                _uiState.update { it.copy(pendingJoinRetaId = null) }
                repository.rollbackUnirse(reta.id, snapshotJugadoresActuales, snapshotListaJugadoresIds)
                _errors.emit(e.message ?: "No se pudo unir a la reta")
            }
        }
    }

    // Listen for server-side errors and rollback pending join
    private fun listenForWsErrors() {
        viewModelScope.launch {
            wsClient.messages.collect { message ->
                if (message is WsIncomingMessage.Error) {
                    val pendingId = _uiState.value.pendingJoinRetaId
                    if (pendingId != null) {
                        // Find the reta snapshot from current state before rollback
                        val reta = _uiState.value.retas.find { it.id == pendingId }
                        if (reta != null) {
                            repository.rollbackUnirse(
                                retaId = pendingId,
                                snapshotJugadoresActuales = reta.jugadoresActuales,
                                snapshotListaJugadores = reta.listaJugadores.map { it.id }
                            )
                        }
                        _uiState.update { it.copy(pendingJoinRetaId = null) }
                    }
                    _errors.emit(message.mensaje)
                }
            }
        }
    }

    // ─── Create reta ─────────────────────────────────────────────────────────

    fun onAbrirCrearBottomSheet() {
        _uiState.update { it.copy(isCrearBottomSheetVisible = true) }
    }

    fun onCerrarCrearBottomSheet() {
        _uiState.update { it.copy(isCrearBottomSheetVisible = false) }
    }

    fun onCrearReta(titulo: String, fechaHora: String, maxJugadores: Int) {
        viewModelScope.launch {
            try {
                crearRetaUseCase(
                    zonaId = _uiState.value.zonaSeleccionada,
                    titulo = titulo,
                    fechaHora = fechaHora,
                    maxJugadores = maxJugadores,
                    creadorId = currentUserId,
                    creadorNombre = currentUserNombre
                )
                _uiState.update { it.copy(isCrearBottomSheetVisible = false) }
            } catch (e: Exception) {
                _errors.emit(e.message ?: "No se pudo crear la reta")
            }
        }
    }

    // ─── Lifecycle ───────────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        repository.desconectar()
    }
}

