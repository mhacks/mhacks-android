package com.mhacks.app.data.repository

import com.mhacks.app.data.models.Event
import com.mhacks.app.data.room.dao.EventDao
import com.mhacks.app.data.service.EventService
import javax.inject.Inject

//class EventsRepository @Inject constructor(
//        private val eventService: EventService,
//        private val eventDao: EventDao) {
//
//    fun getEventCache() = eventDao.getEvents()
//
//    fun getEventRemote() =
//            eventService.getEventResponse()
//
//    fun updateEvent(event: Event) {
//        eventDao.updateEvent(event)
//        return
//    }
//
//
//    fun deleteAndUpdateEvents(events: List<Event>) =
//            eventDao.deleteAndUpdateEvents(events)
//
//}