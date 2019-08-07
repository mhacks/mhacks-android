package org.mhacks.app.events.data.service

import io.reactivex.Single
import org.mhacks.app.data.models.EventsResponse
import retrofit2.http.GET

interface EventService {

    @GET("event")
    fun getEventResponse(): Single<EventsResponse>

}