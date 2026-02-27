package com.jesuscast.gamefotballapp.features.chat.data.mapper

import com.jesuscast.gamefotballapp.features.chat.data.local.entity.ChatMessageEntity
import com.jesuscast.gamefotballapp.features.chat.data.remote.dto.ChatMessageDto
import com.jesuscast.gamefotballapp.features.chat.domain.model.ChatMessage

fun ChatMessageDto.toEntity(): ChatMessageEntity = ChatMessageEntity(
    id = id,
    retaId = retaId,
    usuarioId = usuarioId,
    nombre = nombre,
    texto = texto,
    timestamp = timestamp
)

fun ChatMessageEntity.toDomain(): ChatMessage = ChatMessage(
    id = id,
    retaId = retaId,
    usuarioId = usuarioId,
    nombre = nombre,
    texto = texto,
    timestamp = timestamp
)

