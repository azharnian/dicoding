package com.example.dicodingeventapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.example.dicodingeventapp.data.api.ApiConfig
import com.example.dicodingeventapp.data.api.ApiService
import com.example.dicodingeventapp.data.local.dao.FavoriteEventDao
import com.example.dicodingeventapp.data.local.entity.FavoriteEventEntity
import com.example.dicodingeventapp.data.local.room.AppDatabase
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.data.model.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteEventDao
) {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getEvents(active: Int) {

        _isLoading.value = true

        ApiConfig.getApiService()
            .getEvents(active)
            .enqueue(object : Callback<EventResponse> {

                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {

                    _isLoading.value = false

                    if (response.isSuccessful) {
                        val list = response.body()?.listEvents ?: emptyList()
                        if (active == 1) {
                            _events.value = list
                        } else {
                            _finishedEvents.value = list
                        }
                    } else {
                        _error.value = "Gagal memuat data"
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    _isLoading.value = false
                    _error.value = t.message
                }
            })
    }

    fun searchEvents(query: String) {

        _isLoading.value = true

        ApiConfig.getApiService()
            .searchEvents(-1, query)
            .enqueue(object : Callback<EventResponse> {

                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {

                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _events.value = response.body()?.listEvents
                    } else {
                        _error.value = "Search gagal"
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {

                    _isLoading.value = false
                    _error.value = t.message
                }
            })
    }

    // ================= FAVORITE =================

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

                val apiService = ApiConfig.getApiService()
                val database = AppDatabase.getDatabase(context)
                val favoriteDao = database.favoriteEventDao()

                val instance = EventRepository(apiService, favoriteDao)
                INSTANCE = instance
                instance
            }
        }
    }

}


