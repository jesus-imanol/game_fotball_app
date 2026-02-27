package com.jesuscast.gamefotballapp.features.lobby.data.mapper

import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.JugadorEntity
import com.jesuscast.gamefotballapp.features.lobby.data.local.entity.RetaEntity
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.RetaDto
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta

fun RetaDto.toEntity(zonaId: String): RetaEntity {
    // The API may not return creador_id/creador_nombre explicitly;
    // fall back to the first jugador who is always the creator.
    val primerJugador = listaJugadores.firstOrNull()
    return RetaEntity(
        id = id,
        titulo = titulo,
        fechaHora = fechaHora,
        maxJugadores = maxJugadores,
        jugadoresActuales = jugadoresActuales,
        zonaId = zonaId,
        creadorId = creadorId ?: primerJugador?.usuarioId ?: "",
        creadorNombre = creadorNombre ?: primerJugador?.nombre ?: ""
    )
}

fun RetaEntity.toDomain(jugadores: List<JugadorEntity>): Reta = Reta(
    id = id,
    titulo = titulo,
    fechaHora = fechaHora,
    maxJugadores = maxJugadores,
    jugadoresActuales = jugadoresActuales,
    listaJugadores = jugadores.map { it.toDomain() },
    zonaId = zonaId,
    creadorId = creadorId,
    creadorNombre = creadorNombre
)

