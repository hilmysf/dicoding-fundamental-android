package com.hilmysf.fundamental.domain.model

import com.hilmysf.fundamental.data.local.room.EventEntity

data class Event(
    val id: Int,
    val name: String,
    val summary: String,
    val description: String,
    val imageLogo: String,
    val mediaCover: String,
    val category: String,
    val ownerName: String,
    val cityName: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val endTime: String,
    val link: String,
    val isBookmarked: Boolean = false
) {
    fun toEntity(): EventEntity {
        return EventEntity(
            id = id,
            name = name,
            summary = summary,
            description = description,
            imageLogo = imageLogo,
            mediaCover = mediaCover,
            category = category,
            ownerName = ownerName,
            cityName = cityName,
            quota = quota,
            registrants = registrants,
            beginTime = beginTime,
            endTime = endTime,
            link = link,
        )
    }
}