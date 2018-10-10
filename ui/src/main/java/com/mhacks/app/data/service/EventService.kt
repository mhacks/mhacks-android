package com.mhacks.app.data.service

import com.mhacks.app.data.models.EventsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface EventService {

    @GET("event")
    fun getEventResponse(): Single<EventsResponse>

}