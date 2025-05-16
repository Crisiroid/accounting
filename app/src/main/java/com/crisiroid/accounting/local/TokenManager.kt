package com.crisiroid.accounting.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extension property to create the DataStore
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class TokenManager(private val context: Context) {

    companion object {
        private val USER_TOKEN = stringPreferencesKey("user_token")
    }

    // Save the token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_TOKEN] = token
        }
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_TOKEN]
    }

    suspend fun hasToken(): Boolean {
        return context.dataStore.data.map { prefs ->
            prefs[USER_TOKEN] != null
        }.first()
    }

    suspend fun removeToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(USER_TOKEN)
        }
    }
}