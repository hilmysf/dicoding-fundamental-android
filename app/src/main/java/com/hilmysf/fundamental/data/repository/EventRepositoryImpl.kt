package com.hilmysf.fundamental.data.repository

import com.hilmysf.fundamental.data.local.room.EventDao
import com.hilmysf.fundamental.data.local.room.EventEntity
import com.hilmysf.fundamental.data.remote.network.EventApi
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventApi: EventApi,
    private val eventDao: EventDao
) : EventRepository {
    override fun getEvents(
        query: String?,
        active: Int
    ): Flow<ResultState<List<Event>>> = flow {
        emit(ResultState.Loading)
        try {
            val response = eventApi.getEvents(
                active = active,
                query = query
            )
            val data = response.listEventResponses.map {
                it.toDomain()
            }
            emit(ResultState.Success(data))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    override fun getEventById(id: Int): Flow<ResultState<Event>> = flow {
        emit(ResultState.Loading)
        try {
            val response = eventApi.getEventById(
                id = id
            )
            val data = response.eventResponse.toDomain()
            emit(ResultState.Success(data))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    override fun getOneEvent(): Flow<ResultState<Event>> = flow {
        emit(ResultState.Loading)
        val response = eventApi.getEvents(
            active = 1,
            limit = 1
        )
        val data = response.listEventResponses.map {
            it.toDomain()
        }
        emit(ResultState.Success(data.first()))
    }.catch { e ->
        emit(ResultState.Error(e.message.toString()))
    }

    override fun getBookmarkedEvents(): Flow<ResultState<List<Event>>> {
        return eventDao.getBookmarkedEvents()
            .map<List<EventEntity>, ResultState<List<Event>>> { entityList ->
                val domainList = entityList.map { it.toDomain().copy(isBookmarked = true) }
                ResultState.Success(domainList)
            }
            .onStart {
                emit(ResultState.Loading)
            }
            .catch { e ->
                emit(ResultState.Error(e.message.toString()))
            }
    }

    override fun isEventBookmarked(eventId: Int): Flow<Boolean> {
        return eventDao.isEventBookmarked(eventId)
    }

    override suspend fun insertEvent(event: Event) {
        val eventEntity = event.toEntity()
        eventDao.insertEvent(eventEntity)
    }

    override suspend fun deleteEvent(event: Event) {
        val eventEntity = event.toEntity()
        eventDao.deleteEvent(eventEntity)
    }

    override fun getEventsWithBookmark(
        query: String?,
        active: Int
    ): Flow<ResultState<List<Event>>> {
        return combine(
            getEvents(query, active),
            eventDao.getBookmarkedEvents()
        ) { remoteEvents, bookmarks ->
            when (remoteEvents) {
                is ResultState.Loading -> ResultState.Loading
                is ResultState.Error -> ResultState.Error(remoteEvents.error)
                is ResultState.Success -> {
                    val data = remoteEvents.data.map {
                        val isBookmarked = bookmarks
                            .any { bookmark ->
                                bookmark.id == it.id
                            }
                        it.copy(isBookmarked = isBookmarked)
                    }
                    ResultState.Success(data = data)
                }
            }
        }
    }
}