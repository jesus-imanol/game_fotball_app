package com.jesuscast.gamefotballapp.features.lobby.data.mapper

import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.JugadorEntity
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.RetaEntity
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.RetaDto
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta

fun RetaDto.toEntity(zonaId: String): RetaEntity = RetaEntity(
    id = id,
    titulo = titulo,
    fechaHora = fechaHora,
    maxJugadores = maxJugadores,
    jugadoresActuales = jugadoresActuales,
    zonaId = zonaId
)

fun RetaEntity.toDomain(jugadores: List<JugadorEntity>): Reta = Reta(
    id = id,
    titulo = titulo,
    fechaHora = fechaHora,
    maxJugadores = maxJugadores,
    jugadoresActuales = jugadoresActuales,
    listaJugadores = jugadores.map { it.toDomain() },
    zonaId = zonaId
)

