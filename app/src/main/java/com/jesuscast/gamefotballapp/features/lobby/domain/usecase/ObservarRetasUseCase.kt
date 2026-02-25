package com.jesuscast.gamefotballapp.features.lobby.domain.usecase

import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository
import kotlinx.coroutines.flow.Flow

class ObservarRetasUseCase(
    private val repository: LobbyRepository
) {
    operator fun invoke(zonaId: String): Flow<List<Reta>> =
        repository.observeRetas(zonaId)
}

