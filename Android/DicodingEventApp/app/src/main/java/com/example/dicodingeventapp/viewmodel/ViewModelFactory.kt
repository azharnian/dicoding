package com.example.dicodingeventapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.data.local.room.AppDatabase
import com.example.dicodingeventapp.data.preferences.ThemePreferences
import com.example.dicodingeventapp.data.repository.EventRepository

class ViewModelFactory private constructor(
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {

    private val repository: EventRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        val dao = database.favoriteEventDao()
        EventRepository(
            apiService = com.example.dicodingeventapp.data.api.ApiConfig.getApiService(),
            favoriteDao = dao
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(
                ThemePreferences.getInstance(context)
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}