package org.mhacks.app.events

import org.mhacks.app.data.models.Event
import org.mhacks.app.data.room.dao.EventDao
import org.mhacks.app.events.data.service.EventService
import javax.inject.Inject

class EventRepository @Inject constructor(
        private val eventService: EventService,
        private val eventDao: EventDao) {

    fun getEventCache() = eventDao.getEvents()

    fun getEventRemote() =
            eventService.getEventResponse()

    fun updateEvent(event: Event) {
        eventDao.updateEvent(event)
    }

    fun deleteAndUpdateEvents(events: List<Event>) =
            eventDao.deleteAndUpdateEvents(events)

}