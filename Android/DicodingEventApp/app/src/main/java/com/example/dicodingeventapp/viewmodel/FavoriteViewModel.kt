package com.example.dicodingeventapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.local.entity.FavoriteEventEntity
import com.example.dicodingeventapp.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: EventRepository
) : ViewModel() {

    fun getFavorites(): LiveData<List<FavoriteEventEntity>> {
        return repository.getFavoriteEvents()
    }

    fun getFavoriteById(id: Int): LiveData<FavoriteEventEntity?> {
        return repository.getFavoriteById(id)
    }

    fun addFavorite(event: FavoriteEventEntity) {
        viewModelScope.launch {
            repository.insertFavorite(event)
        }
    }

    fun removeFavorite(event: FavoriteEventEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(event)
        }
    }
}