package com.hilmysf.fundamental.presentation.finished

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
class FinishediewModel @Inject constructor(
    private val getEventsWithBookmarksUseCase: GetEventsWithBookmarksUseCase,
    private val bookmarkHandler: BookmarkHandler
) : ViewModel() {
    val finishedEvents: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData()
    }
    var isFinishedDataLoaded = false

    init {
        getFinishedEvents()
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