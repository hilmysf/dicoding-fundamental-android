package com.hilmysf.fundamental.presentation.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.data.remote.response.Event
import com.hilmysf.fundamental.data.repository.EventRepository
import com.hilmysf.fundamental.domain.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    lateinit var detailEvent: Event
    val event: MutableLiveData<ResultState<Event>> by lazy {
        MutableLiveData<ResultState<Event>>()
    }

    fun getEvent(eventId: Int) {
        viewModelScope.launch {
            eventRepository.getEventById(eventId).collect {
                Log.d("TAG", "getEvents: $it")
                event.postValue(it)
            }
        }
    }
}