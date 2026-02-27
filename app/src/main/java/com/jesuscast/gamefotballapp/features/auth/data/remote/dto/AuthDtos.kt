package com.jesuscast.gamefotballapp.features.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("nombre") val nombre: String
)

data class AuthResponse(
    @SerializedName("status") val status: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("usuario") val usuario: UsuarioDto?
)

data class UsuarioDto(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("nombre") val nombre: String
)
