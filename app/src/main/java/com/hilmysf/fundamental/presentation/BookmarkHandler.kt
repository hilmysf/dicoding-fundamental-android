package com.hilmysf.fundamental.presentation

import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.usecase.DeleteEventBookmarkUseCase
import com.hilmysf.fundamental.domain.usecase.InsertEventBookmarkUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Definisikan Interface
interface BookmarkHandler {
    fun handleBookmark(event: Event, scope: CoroutineScope)
}

// 2. Implementasi
class BookmarkHandlerImpl @Inject constructor(
    private val insertUseCase: InsertEventBookmarkUseCase,
    private val deleteUseCase: DeleteEventBookmarkUseCase
) : BookmarkHandler {
    override fun handleBookmark(event: Event, scope: CoroutineScope) {
        scope.launch {
            if (event.isBookmarked) {
                deleteUseCase(event)
            } else {
                insertUseCase(event)
            }
        }
    }
}