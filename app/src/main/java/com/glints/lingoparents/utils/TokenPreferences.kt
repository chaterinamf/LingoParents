package com.glints.lingoparents.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TokenPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

        @Volatile
        private var INSTANCE: TokenPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>?): TokenPreferences {
            return if (dataStore != null) {
                INSTANCE ?: synchronized(this) {
                    val instance = TokenPreferences(dataStore)
                    INSTANCE = instance
                    instance
                }
            } else {
                INSTANCE!!
            }
        }
    }

    fun getAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getRefreshToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    suspend fun resetToken() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}