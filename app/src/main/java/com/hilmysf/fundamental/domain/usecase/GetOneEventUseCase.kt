package com.hilmysf.fundamental.domain.usecase

import com.hilmysf.fundamental.domain.repository.EventRepository
import javax.inject.Inject

class GetOneEventUseCase @Inject constructor(private val eventRepository: EventRepository) {
    operator fun invoke() = eventRepository.getOneEvent()
}