package com.hilmysf.fundamental.data.remote.network

import com.hilmysf.fundamental.data.remote.request.EventListResponse
import com.hilmysf.fundamental.data.remote.request.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("q") query: String? = null,
    ): EventListResponse

    @GET("events/{id}")
    suspend fun getEventById(
        @Path("id") id: Int,
    ): EventResponse
}