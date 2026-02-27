package com.jesuscast.gamefotballapp.features.lobby.presentation

import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.presentation.components.AlertType

/** Connection states shown in the UI indicator. */
enum class WsConnectionState {
    CONNECTING,
    CONNECTED,
    DISCONNECTED,
    ERROR
}

/**
 * One-shot alert event to surface to the user via [AppAlertDialog].
 * Setting it to null means "no dialog visible".
 */
data class AlertEvent(
    val type: AlertType,
    val title: String,
    val message: String
)

/** Immutable UI state consumed by LobbyScreen */
data class LobbyState(
    val retas: List<Reta> = emptyList(),
    val isLoading: Boolean = true,
    /** retaId of the reta that has a pending optimistic join */
    val pendingJoinRetaId: String? = null,
    val zonas: List<String> = listOf(
        "suchiapa_centro", "suchiapa_norte", "suchiapa_sur"
    ),
    val zonaSeleccionada: String = "suchiapa_centro",
    val isCrearBottomSheetVisible: Boolean = false,
    /** Populated from SessionManager on init */
    val currentUserId: String = "",
    val currentUserNombre: String = "",
    /** Live WebSocket connection status shown in the header */
    val wsConnectionState: WsConnectionState = WsConnectionState.CONNECTING,
    /** One-shot alert dialog event; null = no dialog */
    val alertEvent: AlertEvent? = null
)
