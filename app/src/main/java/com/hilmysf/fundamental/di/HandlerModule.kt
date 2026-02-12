package com.hilmysf.fundamental.di

import com.hilmysf.fundamental.presentation.BookmarkHandler
import com.hilmysf.fundamental.presentation.BookmarkHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class) // Karena digunakan di ViewModel
abstract class HandlerModule {

    @Binds
    abstract fun bindBookmarkHandler(
        bookmarkHandlerImpl: BookmarkHandlerImpl
    ): BookmarkHandler
}