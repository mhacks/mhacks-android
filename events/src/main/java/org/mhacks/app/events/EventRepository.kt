package org.mhacks.app.events

import org.mhacks.app.data.models.Event
import org.mhacks.app.data.models.EventsResponse
import org.mhacks.app.data.models.Result
import org.mhacks.app.events.data.service.EventService
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class EventRepository @Inject constructor(
        private val eventService: EventService
) {

    suspend fun getEvent(): Result<List<Event>> {
        val response = eventService.getEventResponse()
        val result = getData(response)
        return result
    }

    private fun getData(response: Response<EventsResponse>): Result<EventsResponse> {
        return try {
            getResult(response = response, onError = {
                Result.Error(
                        IOException("Error getting stories ${response.code()} ${response.message()}")
                )
            })
        } catch (e: Exception) {
            Result.Error(IOException("Error getting stories", e))
        }
    }

    private inline fun getResult(
            response: Response<EventsResponse>,
            onError: () -> Result.Error
    ): Result<EventsResponse> {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return Result.Success(body)
            }
        }
        return onError.invoke()
    }

//
//    fun updateEvent(event: Event) {
//        eventDao.updateEvent(event)
//        return
//    }
//
//    fun deleteAndUpdateEvents(events: List<Event>) =
//            eventDao.deleteAndUpdateEvents(events)

}