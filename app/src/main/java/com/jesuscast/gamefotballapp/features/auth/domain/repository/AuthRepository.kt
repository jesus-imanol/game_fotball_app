package com.jesuscast.gamefotballapp.features.auth.domain.repository

import com.jesuscast.gamefotballapp.features.auth.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(username: String, password: String, nombre: String): Result<User>
}
