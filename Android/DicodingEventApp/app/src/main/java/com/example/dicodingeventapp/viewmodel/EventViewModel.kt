package com.example.dicodingeventapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.data.repository.EventRepository

class EventViewModel : ViewModel() {

    private val repository = EventRepository()

    val events: LiveData<List<Event>> = repository.events
    val finishedEvents: LiveData<List<Event>> = repository.finishedEvents
    val isLoading: LiveData<Boolean> = repository.isLoading
    val error: LiveData<String> = repository.error

    fun loadUpcoming() {
        repository.getEvents(1)
    }

    fun loadFinished() {
        repository.getEvents(0)
    }

    fun loadHomeEvents() {
        repository.getEvents(1)
        repository.getEvents(0)
    }

    fun search(query: String) {
        repository.searchEvents(query)
    }
}
