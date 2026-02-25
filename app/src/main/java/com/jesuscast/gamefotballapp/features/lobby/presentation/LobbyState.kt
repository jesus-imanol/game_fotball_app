package com.jesuscast.gamefotballapp.features.lobby.presentation

import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta

/** Immutable UI state consumed by LobbyScreen */
data class LobbyState(
    val retas: List<Reta> = emptyList(),
    val isLoading: Boolean = true,
    /** retaId of the reta that has a pending optimistic join */
    val pendingJoinRetaId: String? = null,
    val zonas: List<String> = listOf(
        "zona_norte", "zona_sur", "zona_este", "zona_oeste", "zona_centro"
    ),
    val zonaSeleccionada: String = "zona_norte",
    val isCrearBottomSheetVisible: Boolean = false
)

