package com.hilmysf.fundamental.data.repository

import com.hilmysf.fundamental.data.remote.network.EventApi
import com.hilmysf.fundamental.data.remote.request.Event
import com.hilmysf.fundamental.domain.model.ResultState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface EventRepository {
    fun getEvents(
        query: String? = null,
        active: Int = 0
    ): Flow<ResultState<List<Event>>>

    fun getEventById(id: Int): Flow<ResultState<Event>>
}

class EventRepositoryImpl @Inject constructor(private val eventApi: EventApi) : EventRepository {
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
            val data = response.listEvents
            emit(ResultState.Success(data))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }

    override fun getEventById(id: Int): Flow<ResultState<Event>>  = flow {
        emit(ResultState.Loading)
        try {
            val response = eventApi.getEventById(
                id = id
            )
            val data = response.event
            emit(ResultState.Success(data))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}