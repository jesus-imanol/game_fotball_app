package com.jesuscast.gamefotballapp.features.auth.presentation

data class LoginState(
    val username: String = "",
    val password: String = "",
    val nombre: String = "",
    val isLoginMode: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
