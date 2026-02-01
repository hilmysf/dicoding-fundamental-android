package com.hilmysf.fundamental.presentation.upcoming

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.data.remote.request.Event
import com.hilmysf.fundamental.data.repository.EventRepository
import com.hilmysf.fundamental.domain.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@HiltViewModel
class UpcomingViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    val upcomingEvents: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData<ResultState<List<Event>>>()
    }
    var isUpcomingDataLoaded = false


    init {
        getUpcomingEvents()
    }

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
}