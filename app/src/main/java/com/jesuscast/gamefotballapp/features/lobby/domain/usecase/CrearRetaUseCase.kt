package com.jesuscast.gamefotballapp.features.lobby.domain.usecase

import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository

class CrearRetaUseCase(
    private val repository: LobbyRepository
) {
    suspend operator fun invoke(
        zonaId: String,
        titulo: String,
        fechaHora: String,
        maxJugadores: Int,
        creadorId: String? = null,
        creadorNombre: String
    ) = repository.crearReta(
        zonaId = zonaId,
        titulo = titulo,
        fechaHora = fechaHora,
        maxJugadores = maxJugadores,
        creadorId = creadorId,
        creadorNombre = creadorNombre
    )
}

