package com.jesuscast.gamefotballapp.core.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "volfootball_session")

@Singleton
class SessionManager @Inject constructor(
    private val context: Context
) {
    private val dataStore get() = context.dataStore

    private object Keys {
        val USER_ID = stringPreferencesKey("user_id")
        val USERNAME = stringPreferencesKey("username")
        val NOMBRE = stringPreferencesKey("nombre")
    }

    val userId: Flow<String?> = dataStore.data.map { it[Keys.USER_ID] }
    val username: Flow<String?> = dataStore.data.map { it[Keys.USERNAME] }
    val nombre: Flow<String?> = dataStore.data.map { it[Keys.NOMBRE] }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { prefs ->
        !prefs[Keys.USER_ID].isNullOrBlank()
    }

    suspend fun saveSession(id: String, username: String, nombre: String) {
        dataStore.edit { prefs ->
            prefs[Keys.USER_ID] = id
            prefs[Keys.USERNAME] = username
            prefs[Keys.NOMBRE] = nombre
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    /** Blocking-style read — use only during init. */
    suspend fun getUserIdOnce(): String? = userId.first()
    suspend fun getNombreOnce(): String? = nombre.first()
}

