package com.hilmysf.fundamental.domain.usecase

import com.hilmysf.fundamental.domain.repository.EventRepository
import javax.inject.Inject

class GetEventsWithBookmarksUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    operator fun invoke(
        query: String? = null,
        active: Int = 0
    ) = eventRepository.getEventsWithBookmark(
        query = query,
        active = active
    )
}