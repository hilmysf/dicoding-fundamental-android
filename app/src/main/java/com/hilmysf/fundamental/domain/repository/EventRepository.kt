package com.hilmysf.fundamental.domain.repository

import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import kotlinx.coroutines.flow.Flow


interface EventRepository {
    //Remote Source
    fun getEvents(
        query: String? = null,
        active: Int = 0
    ): Flow<ResultState<List<Event>>>

    fun getEventById(id: Int): Flow<ResultState<Event>>
    fun getOneEvent(): Flow<ResultState<Event>>

    //Local Source
    fun getBookmarkedEvents(): Flow<ResultState<List<Event>>>
    fun isEventBookmarked(eventId: Int): Flow<Boolean>
    suspend fun insertEvent(event: Event)
    suspend fun deleteEvent(event: Event)

    //Combine
    fun getEventsWithBookmark(
        query: String? = null,
        active: Int = 0
    ): Flow<ResultState<List<Event>>>
}