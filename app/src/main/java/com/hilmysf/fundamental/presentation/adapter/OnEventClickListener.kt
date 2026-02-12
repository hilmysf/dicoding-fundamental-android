package com.hilmysf.fundamental.presentation.adapter

import com.hilmysf.fundamental.domain.model.Event

interface OnEventClickListener {
    fun onEventClick(event: Event)
    fun onBookmarkClick(event: Event)
}