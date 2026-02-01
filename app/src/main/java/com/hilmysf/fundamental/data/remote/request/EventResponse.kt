package com.hilmysf.fundamental.data.remote.request

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("event")
    val event: Event
)