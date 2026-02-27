package com.jesuscast.gamefotballapp.features.chat.presentation

import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.presentation.AlertEvent
import com.jesuscast.gamefotballapp.features.lobby.presentation.WsConnectionState

/** Immutable UI state consumed by RetaDetailScreen */
data class RetaDetailState(
    val reta: Reta? = null,
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = true,
    val currentMessage: String = "",
    val currentUserId: String = "",
    val currentUserNombre: String = "",
    val wsConnectionState: WsConnectionState = WsConnectionState.CONNECTING,
    val pendingJoinRetaId: String? = null,
    /** One-shot alert dialog event; null = no dialog */
    val alertEvent: AlertEvent? = null
)

