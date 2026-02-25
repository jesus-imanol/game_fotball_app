package com.jesuscast.gamefotballapp.features.lobby.data.mapper

import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.JugadorEntity
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.JugadorDto
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Jugador

fun JugadorDto.toEntity(retaId: String): JugadorEntity = JugadorEntity(
    id = id,
    nombre = nombre,
    usuarioId = usuarioId,
    retaId = retaId
)

fun JugadorEntity.toDomain(): Jugador = Jugador(
    id = id,
    nombre = nombre,
    usuarioId = usuarioId,
    retaId = retaId
)

fun JugadorDto.toDomain(): Jugador = Jugador(
    id = id,
    nombre = nombre,
    usuarioId = usuarioId,
    retaId = retaId
)

