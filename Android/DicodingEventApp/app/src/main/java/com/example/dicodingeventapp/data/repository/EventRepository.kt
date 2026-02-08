package com.example.dicodingeventapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dicodingeventapp.data.api.ApiConfig
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.data.model.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun onResponse(
        call: Call<EventResponse>,
        response: Response<EventResponse>
    ) {

        _isLoading.value = false

        if (response.isSuccessful && response.body() != null) {

            _events.value = response.body()!!.listEvents

        } else {

            _error.value =
                "Server error: ${response.code()}"
        }
    }

    fun onFailure(call: Call<EventResponse>, t: Throwable) {

        _isLoading.value = false

        _error.value =
            "Koneksi gagal. Periksa internet."
    }

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

                        _events.value = response.body()?.listEvents

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

}