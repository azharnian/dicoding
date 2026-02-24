package com.example.dicodingeventapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferences private constructor(private val context: Context) {

    private val THEME_KEY = booleanPreferencesKey("dark_mode")

    fun getTheme(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreferences? = null

        fun getInstance(context: Context): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreferences(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}