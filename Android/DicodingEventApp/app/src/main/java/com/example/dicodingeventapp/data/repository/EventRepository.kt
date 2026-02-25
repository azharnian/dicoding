package com.example.dicodingeventapp.data.repository

import androidx.lifecycle.LiveData
import android.content.Context
import com.example.dicodingeventapp.data.api.ApiConfig
import com.example.dicodingeventapp.data.api.ApiService
import com.example.dicodingeventapp.data.local.dao.FavoriteEventDao
import com.example.dicodingeventapp.data.local.entity.FavoriteEventEntity
import com.example.dicodingeventapp.data.local.room.AppDatabase
import com.example.dicodingeventapp.data.model.Event

class EventRepository(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteEventDao
) {


    suspend fun getEvents(active: Int): List<Event> {
        return apiService.getEvents(active).listEvents ?: emptyList()
    }

    suspend fun searchEvents(query: String): List<Event> {
        return apiService.searchEvents(-1, query).listEvents ?: emptyList()
    }


    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteDao.getFavoriteEvents()
    }

    fun getFavoriteById(id: Int): LiveData<FavoriteEventEntity?> {
        return favoriteDao.getFavoriteById(id)
    }

    suspend fun insertFavorite(event: FavoriteEventEntity) {
        favoriteDao.insertFavorite(event)
    }

    suspend fun deleteFavorite(event: FavoriteEventEntity) {
        favoriteDao.deleteFavorite(event)
    }

    companion object {

        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(context: Context): EventRepository {
            return INSTANCE ?: synchronized(this) {
                val apiConfig = ApiConfig.getApiService()
                val database = AppDatabase.getDatabase(context)
                val favoriteDao = database.favoriteEventDao()
                val instance = EventRepository(apiConfig, favoriteDao)
                INSTANCE = instance
                instance
            }
        }
    }
}