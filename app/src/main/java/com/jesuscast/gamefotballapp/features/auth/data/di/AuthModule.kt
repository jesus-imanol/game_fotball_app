package com.jesuscast.gamefotballapp.features.auth.data.di

import com.jesuscast.gamefotballapp.core.di.GamesFotballRetrofit
import com.jesuscast.gamefotballapp.core.session.SessionManager
import com.jesuscast.gamefotballapp.features.auth.data.remote.api.AuthApiService
import com.jesuscast.gamefotballapp.features.auth.data.repository.AuthRepositoryImpl
import com.jesuscast.gamefotballapp.features.auth.domain.repository.AuthRepository
import com.jesuscast.gamefotballapp.features.auth.domain.usecase.LoginUseCase
import com.jesuscast.gamefotballapp.features.auth.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideAuthApiService(@GamesFotballRetrofit retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApiService,
        sessionManager: SessionManager
    ): AuthRepository = AuthRepositoryImpl(api, sessionManager)
}

@Module
@InstallIn(ViewModelComponent::class)
object AuthUseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideLoginUseCase(repo: AuthRepository): LoginUseCase = LoginUseCase(repo)

    @Provides
    @ViewModelScoped
    fun provideRegisterUseCase(repo: AuthRepository): RegisterUseCase = RegisterUseCase(repo)
}
