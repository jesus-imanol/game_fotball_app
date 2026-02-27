package com.jesuscast.gamefotballapp.features.lobby.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesuscast.gamefotballapp.core.session.SessionManager
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsIncomingMessage
import com.jesuscast.gamefotballapp.features.lobby.data.remote.websocket.LobbyWebSocketClient
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.CrearRetaUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.ObservarRetasUseCase
import com.jesuscast.gamefotballapp.features.lobby.domain.usecase.UnirseARetaUseCase
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.AlertType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val crearRetaUseCase: CrearRetaUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyState())
    val uiState: StateFlow<LobbyState> = _uiState.asStateFlow()

    init {
        loadSessionData()
        listenConnectionStatus()
        conectarZona(_uiState.value.zonaSeleccionada)
        listenForWsMessages()
    }

    // ─── Session ──────────────────────────────────────────────────────────────

    private fun loadSessionData() {
        viewModelScope.launch {
            val userId = sessionManager.getUserIdOnce() ?: ""
            val nombre = sessionManager.getNombreOnce() ?: ""
            _uiState.update { it.copy(currentUserId = userId, currentUserNombre = nombre) }
        }
    }

    // ─── Alert helpers ────────────────────────────────────────────────────────

    fun onDismissAlert() {
        _uiState.update { it.copy(alertEvent = null) }
    }

    private fun showAlert(type: AlertType, title: String, message: String) {
        _uiState.update { it.copy(alertEvent = AlertEvent(type, title, message)) }
    }

    // ─── WS connection status ─────────────────────────────────────────────────

    private fun listenConnectionStatus() {
        viewModelScope.launch {
            wsClient.connectionStatus.collect { status ->
                when (status) {
                    is LobbyWebSocketClient.ConnectionStatus.Connected -> {
                        _uiState.update { it.copy(wsConnectionState = WsConnectionState.CONNECTED) }
                    }
                    is LobbyWebSocketClient.ConnectionStatus.Disconnected -> {
                        _uiState.update { it.copy(wsConnectionState = WsConnectionState.DISCONNECTED) }
                    }
                }
            }
        }
    }

    // ─── Zone management ─────────────────────────────────────────────────────

    fun onZonaSeleccionada(zona: String) {
        if (zona == _uiState.value.zonaSeleccionada) return
        _uiState.update { it.copy(zonaSeleccionada = zona, isLoading = true, retas = emptyList()) }
        conectarZona(zona)
    }

    private fun conectarZona(zonaId: String) {
        _uiState.update { it.copy(wsConnectionState = WsConnectionState.CONNECTING) }
        repository.conectar(zonaId)
        viewModelScope.launch {
            observarRetasUseCase(zonaId)
                .catch { e ->
                    showAlert(
                        AlertType.ERROR,
                        "Error al cargar retas",
                        e.message ?: "No se pudieron obtener las retas de la zona."
                    )
                }
                .collect { retas ->
                    _uiState.update { it.copy(retas = retas, isLoading = false) }
                }
        }
    }

    // ─── WS message listener (server errors + confirmations) ─────────────────

    private fun listenForWsMessages() {
        viewModelScope.launch {
            wsClient.messages.collect { message ->
                when (message) {
                    is WsIncomingMessage.Error -> {
                        // Ignore harmless error from initial zona_id subscription
                        if (message.mensaje.contains("no reconocida", ignoreCase = true)) return@collect

                        // Rollback optimistic join if one was pending
                        val pendingId = _uiState.value.pendingJoinRetaId
                        if (pendingId != null) {
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
                        showAlert(
                            AlertType.ERROR,
                            "Error del servidor",
                            "Mensaje: ${message.mensaje}\n\n" +
                                "Zona: ${_uiState.value.zonaSeleccionada}\n" +
                                "Tu userId: ${_uiState.value.currentUserId.take(12)}..."
                        )
                    }
                    is WsIncomingMessage.NuevaReta -> {
                        // Reta created successfully — replace any pending INFO alert
                        showAlert(
                            AlertType.SUCCESS,
                            "¡Reta creada! 🎉",
                            "\"${message.reta.titulo}\" está en línea.\n" +
                                "Jugadores: ${message.reta.jugadoresActuales}/${message.reta.maxJugadores}\n\n" +
                                "¡Los jugadores ya pueden unirse!"
                        )
                    }
                    is WsIncomingMessage.Actualizacion -> {
                        // If we had a pending join for this reta, it's confirmed now
                        val pendingId = _uiState.value.pendingJoinRetaId
                        if (pendingId != null && pendingId == message.retaId) {
                            _uiState.update { it.copy(pendingJoinRetaId = null) }
                            showAlert(
                                AlertType.SUCCESS,
                                "¡Te uniste! ⚽",
                                "Jugadores ahora: ${message.jugadoresActuales}. ¡Nos vemos en la cancha!"
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    // ─── Optimistic join ─────────────────────────────────────────────────────

    fun onUnirse(reta: Reta) {
        if (_uiState.value.pendingJoinRetaId != null) return

        val state = _uiState.value

        // Guard: session loaded?
        if (state.currentUserId.isBlank()) {
            showAlert(
                AlertType.ERROR,
                "Sesión no disponible",
                "No se encontró tu usuario (userId vacío).\n\nCierra la app, inicia sesión de nuevo e intenta otra vez."
            )
            return
        }

        // Guard: already joined?
        val alreadyIn = reta.listaJugadores.any { it.usuarioId == state.currentUserId }
        if (alreadyIn) {
            showAlert(
                AlertType.INFO,
                "Ya estás inscrito",
                "Ya formas parte de \"${reta.titulo}\". ¡Nos vemos en la cancha! ⚽"
            )
            return
        }

        if (state.wsConnectionState != WsConnectionState.CONNECTED) {
            showAlert(
                AlertType.NO_CONNECTION,
                "Sin conexión",
                "Estado: ${state.wsConnectionState.name}\n\nNo se puede enviar. Verifica tu internet."
            )
            return
        }

        val snapshotJugadoresActuales = reta.jugadoresActuales
        val snapshotListaJugadoresIds = reta.listaJugadores.map { it.id }
        _uiState.update { it.copy(pendingJoinRetaId = reta.id) }

        // Show immediate feedback
        showAlert(
            AlertType.INFO,
            "Uniéndose a la reta...",
            "Enviando solicitud para \"${reta.titulo}\".\n\n" +
                "Usuario: ${state.currentUserNombre}\n" +
                "Zona: ${state.zonaSeleccionada}"
        )

        viewModelScope.launch {
            try {
                unirseARetaUseCase(
                    zonaId = state.zonaSeleccionada,
                    retaId = reta.id,
                    usuarioId = state.currentUserId,
                    nombre = state.currentUserNombre
                )
                // send() succeeded — wait for server actualizacion or error
                // listenForWsMessages handles Actualizacion (shows SUCCESS) and Error (shows ERROR)
                // Add timeout: if no response in 8 seconds, warn user
                launch {
                    kotlinx.coroutines.delay(8_000)
                    if (_uiState.value.pendingJoinRetaId == reta.id) {
                        _uiState.update { it.copy(pendingJoinRetaId = null) }
                        showAlert(
                            AlertType.WARNING,
                            "Sin respuesta del servidor",
                            "El mensaje se envió pero no hubo confirmación en 8 segundos.\n\n" +
                                "• retaId: ${reta.id.take(8)}...\n" +
                                "• userId: ${state.currentUserId.take(8)}...\n" +
                                "• Zona: ${state.zonaSeleccionada}\n\n" +
                                "Puede que te hayas unido. Cambia de zona y regresa para verificar."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(pendingJoinRetaId = null) }
                repository.rollbackUnirse(reta.id, snapshotJugadoresActuales, snapshotListaJugadoresIds)
                showAlert(
                    AlertType.ERROR,
                    "No te pudiste unir",
                    "Error al enviar: ${e.message}\n\n" +
                        "• Zona: ${state.zonaSeleccionada}\n" +
                        "• userId: ${state.currentUserId.take(12)}...\n" +
                        "• Reta: ${reta.titulo}"
                )
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

    fun onCrearReta(titulo: String, fechaHora: String, maxJugadores: Int, creadorNombre: String) {
        val state = _uiState.value

        // Guard 1: session data loaded?
        if (state.currentUserId.isBlank()) {
            showAlert(
                AlertType.ERROR,
                "Sesión no disponible",
                "No se encontró tu usuario. Cierra sesión e inicia de nuevo."
            )
            return
        }

        // Guard 2: WebSocket must be open before trying to send
        if (state.wsConnectionState != WsConnectionState.CONNECTED) {
            showAlert(
                AlertType.NO_CONNECTION,
                "Sin conexión al servidor",
                "Estado actual: ${state.wsConnectionState.name}\n\nNo se puede crear la reta porque no hay conexión activa.\n\nRevisa tu internet y vuelve a intentarlo."
            )
            return
        }

        _uiState.update { it.copy(isCrearBottomSheetVisible = false) }

        // Show "sending" feedback
        showAlert(
            AlertType.INFO,
            "Enviando reta...",
            "Publicando \"$titulo\" en la zona ${state.zonaSeleccionada.replace("_", " ")}.\n\nEspera un momento..."
        )

        viewModelScope.launch {
            try {
                val nombreFinal = creadorNombre.ifBlank { state.currentUserNombre }

                crearRetaUseCase(
                    zonaId = state.zonaSeleccionada,
                    titulo = titulo,
                    fechaHora = fechaHora,
                    maxJugadores = maxJugadores,
                    creadorId = state.currentUserId,
                    creadorNombre = nombreFinal
                )

                // Message was sent successfully over the WebSocket.
                // Now wait for server confirmation (NuevaReta) or error.
                // The listenForWsMessages() will show SUCCESS or ERROR alert.
                // But add a timeout: if nothing arrives in 10s, warn the user.
                launch {
                    kotlinx.coroutines.delay(10_000)
                    // If the alert is still the INFO "Enviando...", replace with a warning
                    val current = _uiState.value.alertEvent
                    if (current != null && current.type == AlertType.INFO && current.title.contains("Enviando")) {
                        showAlert(
                            AlertType.WARNING,
                            "Sin respuesta del servidor",
                            "El mensaje se envió pero el servidor no respondió en 10 segundos.\n\n" +
                                "• Verifica que la zona \"${state.zonaSeleccionada}\" existe\n" +
                                "• Tu userId: ${state.currentUserId.take(8)}...\n" +
                                "• Nombre: $nombreFinal\n" +
                                "• Fecha: $fechaHora\n\n" +
                                "La reta pudo haberse creado. Cambia de zona y regresa para refrescar."
                        )
                    }
                }
            } catch (e: Exception) {
                showAlert(
                    AlertType.ERROR,
                    "No se pudo crear la reta",
                    "Error al enviar: ${e.message}\n\n" +
                        "• WS conectado: ${state.wsConnectionState.name}\n" +
                        "• Zona: ${state.zonaSeleccionada}\n" +
                        "• userId: ${state.currentUserId.take(8)}..."
                )
            }
        }
    }

    // ─── Lifecycle ───────────────────────────────────────────────────────────

    override fun onCleared() {
        super.onCleared()
        repository.desconectar()
    }
}

