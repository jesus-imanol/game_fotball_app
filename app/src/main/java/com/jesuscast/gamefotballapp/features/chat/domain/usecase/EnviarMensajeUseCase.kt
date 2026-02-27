package com.jesuscast.gamefotballapp.features.chat.domain.usecase

import com.jesuscast.gamefotballapp.features.chat.domain.repository.ChatRepository

class EnviarMensajeUseCase(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(
        retaId: String,
        usuarioId: String,
        nombre: String,
        mensaje: String
    ) = repository.enviarMensaje(
        retaId = retaId,
        usuarioId = usuarioId,
        nombre = nombre,
        mensaje = mensaje
    )
}

