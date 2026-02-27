package com.jesuscast.gamefotballapp.features.chat.domain.usecase

import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage
import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ObservarMensajesUseCase(
    private val repository: ChatRepository
) {
    operator fun invoke(retaId: String): Flow<List<ChatMessage>> =
        repository.observeMessages(retaId)
}

