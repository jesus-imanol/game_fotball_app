package com.jesuscast.gamefotballapp.features.auth.data.repository

import com.jesuscast.gamefotballapp.core.session.SessionManager
import com.jesuscast.gamefotballapp.features.auth.data.remote.api.AuthApiService
import com.jesuscast.gamefotballapp.features.auth.data.remote.dto.LoginRequest
import com.jesuscast.gamefotballapp.features.auth.data.remote.dto.RegisterRequest
import com.jesuscast.gamefotballapp.features.auth.domain.model.User
import com.jesuscast.gamefotballapp.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> = runCatching {
        val response = api.login(LoginRequest(username, password))
        val usuario = response.usuario
            ?: throw Exception(response.mensaje)
        if (response.status != "success") throw Exception(response.mensaje)
        val user = User(id = usuario.id, username = usuario.username, nombre = usuario.nombre)
        sessionManager.saveSession(user.id, user.username, user.nombre)
        user
    }

    override suspend fun register(username: String, password: String, nombre: String): Result<User> = runCatching {
        val response = api.register(RegisterRequest(username, password, nombre))
        val usuario = response.usuario
            ?: throw Exception(response.mensaje)
        if (response.status != "success") throw Exception(response.mensaje)
        val user = User(id = usuario.id, username = usuario.username, nombre = usuario.nombre)
        sessionManager.saveSession(user.id, user.username, user.nombre)
        user
    }
}
