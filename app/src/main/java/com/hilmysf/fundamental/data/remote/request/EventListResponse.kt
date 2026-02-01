package com.hilmysf.fundamental.data.remote.request

import com.google.gson.annotations.SerializedName

data class EventListResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("listEvents")
    val listEvents: List<Event>,
)
