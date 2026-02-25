package com.example.dicodingeventapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.model.Event
import com.example.dicodingeventapp.data.repository.EventRepository
import com.example.dicodingeventapp.utils.ResultState
import kotlinx.coroutines.launch

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _upcomingState = MutableLiveData<ResultState<List<Event>>>()
    val upcomingState: LiveData<ResultState<List<Event>>> = _upcomingState

    private val _finishedState = MutableLiveData<ResultState<List<Event>>>()
    val finishedState: LiveData<ResultState<List<Event>>> = _finishedState

    private val _searchState = MutableLiveData<ResultState<List<Event>>>()
    val searchState: LiveData<ResultState<List<Event>>> = _searchState


    fun loadUpcoming() {
        viewModelScope.launch {
            _upcomingState.value = ResultState.Loading
            try {
                _upcomingState.value = ResultState.Success(repository.getEvents(1))
            } catch (e: Exception) {
                _upcomingState.value = ResultState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun loadFinished() {
        viewModelScope.launch {
            _finishedState.value = ResultState.Loading
            try {
                _finishedState.value = ResultState.Success(repository.getEvents(0))
            } catch (e: Exception) {
                _finishedState.value = ResultState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun loadHomeEvents() {
        loadUpcoming()
        loadFinished()
    }

    fun search(query: String) {
        viewModelScope.launch {
            _searchState.value = ResultState.Loading
            try {
                _searchState.value = ResultState.Success(repository.searchEvents(query))
            } catch (e: Exception) {
                _searchState.value = ResultState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}