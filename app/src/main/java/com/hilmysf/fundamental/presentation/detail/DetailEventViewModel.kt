package com.hilmysf.fundamental.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    lateinit var detailEvent: Event
    val eventResponse: MutableLiveData<ResultState<Event>> by lazy {
        MutableLiveData<ResultState<Event>>()
    }

    fun getEvent(eventId: Int) {
        viewModelScope.launch {
            eventRepository.getEventById(eventId).collect {
                eventResponse.postValue(it)
            }
        }
    }
}