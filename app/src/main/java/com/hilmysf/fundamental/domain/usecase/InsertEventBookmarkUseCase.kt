package com.hilmysf.fundamental.domain.usecase

import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.repository.EventRepository
import javax.inject.Inject

class InsertEventBookmarkUseCase @Inject constructor(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(event: Event) {
        eventRepository.insertEvent(event)
    }
}