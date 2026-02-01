package com.hilmysf.fundamental.presentation.finished

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
class FinishediewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    val finishedEvents: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData<ResultState<List<Event>>>()
    }
    var isFinishedDataLoaded = false

    init {
        getFinishedEvents()
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