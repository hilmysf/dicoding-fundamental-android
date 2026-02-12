package com.hilmysf.fundamental.domain.usecase

import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedEventsUseCase @Inject constructor(
    private val repository: EventRepository
) {
    operator fun invoke(): Flow<ResultState<List<Event>>> {
        return repository.getBookmarkedEvents()
    }
}