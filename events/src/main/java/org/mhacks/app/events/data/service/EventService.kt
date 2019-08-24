package org.mhacks.app.events.data.service

import io.reactivex.Single
import org.mhacks.app.events.data.model.EventsResponse
import retrofit2.http.GET

interface EventService {

    @GET("event")
    fun getEventResponse(): Single<EventsResponse>

}