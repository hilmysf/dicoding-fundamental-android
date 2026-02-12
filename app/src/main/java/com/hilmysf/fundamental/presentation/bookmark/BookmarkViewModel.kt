package com.hilmysf.fundamental.presentation.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.domain.usecase.DeleteEventBookmarkUseCase
import com.hilmysf.fundamental.domain.usecase.GetBookmarkedEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarkedEventsUseCase: GetBookmarkedEventsUseCase,
    private val deleteEventBookmarkUseCase: DeleteEventBookmarkUseCase,
) : ViewModel() {
    val events: MutableLiveData<ResultState<List<Event>>> by lazy {
        MutableLiveData<ResultState<List<Event>>>()
    }

    fun getBookmarkedEvents() {
        viewModelScope.launch {
            getBookmarkedEventsUseCase(
            ).debounce(500L).collect {
                events.postValue(it)
            }
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            deleteEventBookmarkUseCase(event)
        }
    }
}
