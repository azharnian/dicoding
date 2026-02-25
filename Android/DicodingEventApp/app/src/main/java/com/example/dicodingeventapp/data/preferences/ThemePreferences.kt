package com.example.dicodingeventapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class ThemePreferences private constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val themeKey = booleanPreferencesKey("dark_mode")
    private val dailyReminderKey = booleanPreferencesKey("daily_reminder")

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDark
        }
    }

    fun getDailyReminder(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[dailyReminderKey] ?: false
        }
    }

    suspend fun saveDailyReminder(isActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[dailyReminderKey] = isActive
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemePreferences? = null

        fun getInstance(context: Context): ThemePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreferences(context.applicationContext.dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}