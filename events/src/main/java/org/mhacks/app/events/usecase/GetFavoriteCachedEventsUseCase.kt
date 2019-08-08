package org.mhacks.app.events.usecase

import io.reactivex.Single
import org.mhacks.app.data.model.Event
import org.mhacks.app.events.EventRepository
import org.mhacks.app.usecase.SingleUseCase
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