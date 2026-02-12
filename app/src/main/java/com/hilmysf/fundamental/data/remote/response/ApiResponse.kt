package com.hilmysf.fundamental.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("event")
    val eventResponse: EventResponse
)