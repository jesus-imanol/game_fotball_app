package com.jesuscast.gamefotballapp.features.chat.domain.usecase

import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository

class EnviarMensajeUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        usuarioId: String,
        texto: String
    ) = repository.enviarMensaje(
        usuarioId = usuarioId,
        texto = texto
    )
}

