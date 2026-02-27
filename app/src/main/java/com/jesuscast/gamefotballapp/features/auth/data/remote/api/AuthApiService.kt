package com.jesuscast.gamefotballapp.features.auth.data.remote.api

import com.jesuscast.gamefotballapp.features.auth.data.remote.dto.AuthResponse
import com.jesuscast.gamefotballapp.features.auth.data.remote.dto.LoginRequest
import com.jesuscast.gamefotballapp.features.auth.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/usuarios/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/usuarios/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}
