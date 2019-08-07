package org.mhacks.app.events

import org.mhacks.app.events.data.service.EventService
import javax.inject.Inject

class EventRepository @Inject constructor(
        private val eventService: EventService
) {

    fun getEvent() = eventService.getEventResponse()

}