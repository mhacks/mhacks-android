package org.mhacks.app.events.data.service

import org.mhacks.app.data.models.EventsResponse
import retrofit2.Response
import retrofit2.http.GET

interface EventService {

    @GET("event")
    suspend fun getEventResponse(): Response<EventsResponse>

}