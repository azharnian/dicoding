package com.example.dicodingeventapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.data.preferences.ThemePreferences
import com.example.dicodingeventapp.data.repository.EventRepository

class ViewModelFactory private constructor(
    private val repository: EventRepository,
    private val themePreferences: ThemePreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            return EventViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(themePreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {

        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(
                    repository = EventRepository.getInstance(context.applicationContext),
                    themePreferences = ThemePreferences.getInstance(context.applicationContext)
                )
                INSTANCE = instance
                instance
            }
        }
    }
}