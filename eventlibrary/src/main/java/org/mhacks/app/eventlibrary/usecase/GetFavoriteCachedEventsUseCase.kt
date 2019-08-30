package org.mhacks.app.eventlibrary.usecase

import io.reactivex.Single
import org.mhacks.app.eventlibrary.data.model.Event
import org.mhacks.app.eventlibrary.EventRepository
import org.mhacks.app.core.usecase.SingleUseCase
import javax.inject.Inject

class GetFavoriteCachedEventsUseCase @Inject constructor(
        private val eventRepository: EventRepository
) : SingleUseCase<Unit, List<Event>>() {

    override fun getSingle(parameters: Unit): Single<List<Event>> =
            eventRepository
                    .getEventCache()
                    .map {
                        it.filter { events ->
                            events.favorited
                        }
                    }
}