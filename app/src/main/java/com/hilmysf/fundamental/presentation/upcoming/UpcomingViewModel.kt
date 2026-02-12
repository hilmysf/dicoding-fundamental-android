package com.hilmysf.fundamental.presentation.upcoming

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
class UpcomingViewModel @Inject constructor(
    private val getEventsWithBookmarksUseCase: GetEventsWithBookmarksUseCase,
    private val bookmarkHandler: BookmarkHandler
) : ViewModel() {
    val upcomingEvents: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData()
    }
    var isUpcomingDataLoaded = false


    init {
        getUpcomingEvents()
    }

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
    fun onBookmarkClick(event: Event) {
        bookmarkHandler.handleBookmark(event, viewModelScope)
    }
}