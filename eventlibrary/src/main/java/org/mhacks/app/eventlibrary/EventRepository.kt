package org.mhacks.app.eventlibrary

import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.data.db.EventDao
import org.mhacks.app.eventlibrary.data.service.EventService
import javax.inject.Inject

class EventRepository @Inject constructor(
        private val eventService: EventService,
        private val eventDao: EventDao

) {

    fun getEventCache() = eventDao.getEvents()

    fun getEventRemote() =
            eventService.getEventResponse()

    fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }

    fun deleteAndUpdateEvents(events: List<Event>) =
            eventDao.deleteAndUpdateEvents(events)

}