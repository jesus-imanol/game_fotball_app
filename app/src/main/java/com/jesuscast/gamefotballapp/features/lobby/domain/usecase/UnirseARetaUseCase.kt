package com.jesuscast.gamefotballapp.features.lobby.domain.usecase

import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository

class UnirseARetaUseCase(
    private val repository: LobbyRepository
) {
    suspend operator fun invoke(
        zonaId: String,
        retaId: String,
        usuarioId: String,
        nombre: String
    ) = repository.unirse(
        zonaId = zonaId,
        retaId = retaId,
        usuarioId = usuarioId,
        nombre = nombre
    )
}

