package com.example.dicodingeventapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.preferences.ThemePreferences
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val pref: ThemePreferences
) : ViewModel() {

    val isDarkTheme = pref.getTheme().asLiveData()

    fun saveTheme(isDark: Boolean) {
        viewModelScope.launch {
            pref.saveTheme(isDark)
        }
    }
}