package com.hilmysf.fundamental.data.local.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.hilmysf.fundamental.domain.model.Event

@Entity(tableName = "bookmarked_events")
data class EventEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,

    @ColumnInfo(name = "summary")
    @SerializedName("summary")
    val summary: String,

    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String,

    @ColumnInfo(name = "image_logo")
    @SerializedName("imageLogo")
    val imageLogo: String,

    @ColumnInfo(name = "media_cover")
    @SerializedName("mediaCover")
    val mediaCover: String,

    @ColumnInfo(name = "category")
    @SerializedName("category")
    val category: String,

    @ColumnInfo(name = "owner_name")
    @SerializedName("ownerName")
    val ownerName: String,

    @ColumnInfo(name = "city_name")
    @SerializedName("cityName")
    val cityName: String,

    @ColumnInfo(name = "quota")
    @SerializedName("quota")
    val quota: Int,

    @ColumnInfo(name = "registrants")
    @SerializedName("registrants")
    val registrants: Int,

    @ColumnInfo(name = "begin_time")
    @SerializedName("beginTime")
    val beginTime: String,

    @ColumnInfo(name = "end_time")
    @SerializedName("endTime")
    val endTime: String,

    @ColumnInfo(name = "link")
    @SerializedName("link")
    val link: String
) {
    fun toDomain(): Event {
        return Event(
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
            link = link
        )
    }
}