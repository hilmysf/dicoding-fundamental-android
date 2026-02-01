package com.hilmysf.fundamental.presentation.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.data.remote.response.Event
import com.hilmysf.fundamental.data.repository.EventRepository
import com.hilmysf.fundamental.domain.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    val upcomingEvents: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData<ResultState<List<Event>>>()
    }
    val finishedEvents: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData<ResultState<List<Event>>>()
    }
    var isUpcomingDataLoaded = false
    var isFinishedDataLoaded = false


    fun getUpcomingEvents(forceLoad: Boolean = false, query: String? = null) {
        if (forceLoad || !isUpcomingDataLoaded) {
            viewModelScope.launch {
                eventRepository.getEvents(
                    active = 1,
                    query = query
                ).debounce(500L).collect {
                    Log.d("TAG", "getEvents: $it")
                    upcomingEvents.postValue(it)
                    if (it is ResultState.Success) {
                        isUpcomingDataLoaded = true
                    }
                }
            }
        }
    }

    fun getFinishedEvents(forceLoad: Boolean = false, query: String? = null) {
        if (forceLoad || !isFinishedDataLoaded) {
            viewModelScope.launch {
                eventRepository.getEvents(
                    active = 0,
                    query = query
                ).debounce(500L).collect {
                    Log.d("TAG", "getEvents: $it")
                    finishedEvents.postValue(it)
                    if (it is ResultState.Success) {
                        isFinishedDataLoaded = true
                    }
                }
            }
        }
    }
}