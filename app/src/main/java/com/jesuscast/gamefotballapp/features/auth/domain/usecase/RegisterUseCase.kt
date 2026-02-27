package com.jesuscast.gamefotballapp.features.auth.domain.usecase

import com.jesuscast.gamefotballapp.features.auth.domain.model.User
import com.jesuscast.gamefotballapp.features.auth.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String, nombre: String): Result<User> =
        repository.register(username, password, nombre)
}
