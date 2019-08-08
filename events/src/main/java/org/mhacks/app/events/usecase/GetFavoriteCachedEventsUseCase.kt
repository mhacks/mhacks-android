package org.mhacks.app.events.usecase

import io.reactivex.Scheduler
import io.reactivex.Single
import org.mhacks.app.data.models.Event
import org.mhacks.app.events.EventRepository
import org.mhacks.app.usecase.SingleUseCase
import javax.inject.Inject

class GetFavoriteCachedEventsUseCase @Inject constructor(
        override val threadExecutor: Scheduler,
        override val mainThreadExecutor: Scheduler,
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