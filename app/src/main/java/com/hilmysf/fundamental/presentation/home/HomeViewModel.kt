package com.hilmysf.fundamental.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.domain.usecase.GetEventsWithBookmarksUseCase
import com.hilmysf.fundamental.presentation.BookmarkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEventsWithBookmarksUseCase: GetEventsWithBookmarksUseCase,
    private val bookmarkHandler: BookmarkHandler
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
                getEventsWithBookmarksUseCase(
                    active = 1,
                    query = query
                ).debounce(500L).collect {
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
                getEventsWithBookmarksUseCase(
                    active = 0,
                    query = query
                ).debounce(500L).collect {
                    finishedEvents.postValue(it)
                    if (it is ResultState.Success) {
                        isFinishedDataLoaded = true
                    }
                }
            }
        }
    }
    fun onBookmarkClick(event: Event) {
        bookmarkHandler.handleBookmark(event, viewModelScope)
    }
}