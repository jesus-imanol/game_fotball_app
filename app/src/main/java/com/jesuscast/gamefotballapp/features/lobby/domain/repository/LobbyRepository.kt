package com.jesuscast.gamefotballapp.features.lobby.domain.repository

import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import kotlinx.coroutines.flow.Flow

interface LobbyRepository {

    /** Observes the list of Retas for a zone from Room (SSOT). */
    fun observeRetas(zonaId: String): Flow<List<Reta>>

    /** Connects the WebSocket for the given zone. */
    fun conectar(zonaId: String)

    /** Disconnects the WebSocket. */
    fun desconectar()

    /**
     * Optimistically joins a reta (updates Room immediately).
     * Sends the WS message. If an error is received the caller is
     * responsible for rolling back via [rollbackUnirse].
     */
    suspend fun unirse(
        zonaId: String,
        retaId: String,
        usuarioId: String,
        nombre: String
    )

    /** Reverts an optimistic join for [retaId] restoring the previous snapshot. */
    suspend fun rollbackUnirse(retaId: String, snapshotJugadoresActuales: Int, snapshotListaJugadores: List<String>)

    /**
     * Sends a "crear" action through the WebSocket.
     */
    suspend fun crearReta(
        zonaId: String,
        titulo: String,
        fechaHora: String,
        maxJugadores: Int,
        creadorId: String,
        creadorNombre: String
    )
}

