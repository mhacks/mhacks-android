package org.mhacks.app.eventlibrary.data.service

import io.reactivex.Single
import org.mhacks.app.eventlibrary.data.model.EventsResponse
import retrofit2.http.GET

interface EventService {

    @GET("event")
    fun getEventResponse(): Single<EventsResponse>

}