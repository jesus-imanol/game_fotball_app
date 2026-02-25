package com.jesuscast.gamefotballapp.features.lobby.domain.model

data class Reta(
    val id: String,
    val titulo: String,
    val fechaHora: String,
    val maxJugadores: Int,
    val jugadoresActuales: Int,
    val listaJugadores: List<Jugador>,
    val zonaId: String
)

