package com.jesuscast.gamefotballapp.features.lobby.data.repository

import com.google.gson.Gson
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.JugadorDao
import com.jesuscast.gamefotballapp.features.lobby.data.local.dao.RetaDao
import com.jesuscast.gamefotballapp.features.lobby.data.mapper.toEntity
import com.jesuscast.gamefotballapp.features.lobby.data.mapper.toDomain
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsCrearRetaRequest
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsIncomingMessage
import com.jesuscast.gamefotballapp.features.lobby.data.remote.dto.WsUnirseRetaRequest
import com.jesuscast.gamefotballapp.features.lobby.data.remote.websocket.LobbyWebSocketClient
import com.jesuscast.gamefotballapp.features.lobby.domain.model.Reta
import com.jesuscast.gamefotballapp.features.lobby.domain.repository.LobbyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LobbyRepositoryImpl(
    private val wsClient: LobbyWebSocketClient,
    private val retaDao: RetaDao,
    private val jugadorDao: JugadorDao,
    private val gson: Gson
) : LobbyRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentZonaId: String = ""

    // ─── SSOT: WebSocket → Room ──────────────────────────────────────────────
    init {
        scope.launch {
            wsClient.messages.collect { message ->
                when (message) {
                    is WsIncomingMessage.RetasZona -> {
                        // Initial load: replace all retas for this zone in Room
                        retaDao.deleteByZona(currentZonaId)
                        message.retas.forEach { dto ->
                            retaDao.upsert(dto.toEntity(currentZonaId))
                            jugadorDao.deleteByReta(dto.id)
                            jugadorDao.upsertAll(dto.listaJugadores.map { it.toEntity(dto.id) })
                        }
                    }
                    is WsIncomingMessage.NuevaReta -> {
                        val dto = message.reta
                        retaDao.upsert(dto.toEntity(currentZonaId))
                        jugadorDao.deleteByReta(dto.id)
                        jugadorDao.upsertAll(dto.listaJugadores.map { it.toEntity(dto.id) })
                    }
                    is WsIncomingMessage.Actualizacion -> {
                        retaDao.updateJugadoresActuales(
                            retaId = message.retaId,
                            jugadoresActuales = message.jugadoresActuales
                        )
                        jugadorDao.deleteByReta(message.retaId)
                        jugadorDao.upsertAll(
                            message.listaJugadores.map { it.toEntity(message.retaId) }
                        )
                    }
                    // Errors handled at ViewModel level via wsClient.messages collector
                    else -> Unit
                }
            }
        }
    }

    // ─── LobbyRepository ─────────────────────────────────────────────────────

    /**
     * SSOT: Room emits whenever retas change. For each emission we fetch
     * the jugadores synchronously (same IO dispatcher) and map to domain.
     */
    override fun observeRetas(zonaId: String): Flow<List<Reta>> =
        retaDao.observeByZona(zonaId).map { retaEntities ->
            retaEntities.map { retaEntity ->
                val jugadores = jugadorDao.getByReta(retaEntity.id)
                retaEntity.toDomain(jugadores)
            }
        }

    override fun conectar(zonaId: String) {
        currentZonaId = zonaId
        wsClient.disconnect()
        wsClient.connect(zonaId)
    }

    override fun desconectar() {
        wsClient.disconnect()
    }

    override suspend fun unirse(
        zonaId: String,
        retaId: String,
        usuarioId: String,
        nombre: String
    ) {
        val request = WsUnirseRetaRequest(
            zonaId = zonaId,
            retaId = retaId,
            usuarioId = usuarioId,
            nombre = nombre
        )
        val json = gson.toJson(request)
        val sent = wsClient.send(json)
        if (!sent) {
            throw Exception("No se pudo enviar el mensaje. El WebSocket no está conectado.")
        }
    }

    override suspend fun rollbackUnirse(
        retaId: String,
        snapshotJugadoresActuales: Int,
        snapshotListaJugadores: List<String>
    ) {
        // Restore the jugadoresActuales count
        retaDao.updateJugadoresActuales(
            retaId = retaId,
            jugadoresActuales = snapshotJugadoresActuales
        )
        // Delete current jugadores for this reta and restore only the snapshot ones
        val currentJugadores = jugadorDao.getByReta(retaId)
        jugadorDao.deleteByReta(retaId)
        val snapshotEntities = currentJugadores.filter { it.id in snapshotListaJugadores }
        jugadorDao.upsertAll(snapshotEntities)
    }

    override suspend fun crearReta(
        zonaId: String,
        titulo: String,
        fechaHora: String,
        maxJugadores: Int,
        creadorId: String?,
        creadorNombre: String
    ) {
        val request = WsCrearRetaRequest(
            zonaId = zonaId,
            titulo = titulo,
            fechaHora = fechaHora,
            maxJugadores = maxJugadores,
            creadorId = creadorId,
            creadorNombre = creadorNombre
        )
        val json = gson.toJson(request)
        val sent = wsClient.send(json)
        if (!sent) {
            throw Exception("No se pudo enviar el mensaje. El WebSocket no está conectado.")
        }
    }
}

